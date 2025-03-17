package net.jjjshop.common.factory.paysuccess.type.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.entity.user.UserBalanceLog;
import net.jjjshop.common.enums.BalanceLogEnum;
import net.jjjshop.common.enums.OrderPayTypeEnum;
import net.jjjshop.common.factory.paysuccess.source.PaySuccessSourceFactory;
import net.jjjshop.common.factory.paysuccess.type.PaySuccessOrder;
import net.jjjshop.common.factory.paysuccess.type.PaySuccessTypeFactoryService;
import net.jjjshop.common.factory.product.ProductFactory;
import net.jjjshop.common.service.order.OrderProductService;
import net.jjjshop.common.service.order.OrderService;
import net.jjjshop.common.service.user.UserBalanceLogService;
import net.jjjshop.common.service.user.UserService;
import net.jjjshop.common.util.OrderUtils;
import net.jjjshop.common.vo.order.PayDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付成功
 */
@Slf4j
@Service
public class MasterPaySuccessTypeFactoryService extends PaySuccessTypeFactoryService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductFactory productFactory;
    @Autowired
    private OrderProductService orderProductService;
    @Autowired
    private UserBalanceLogService userBalanceLogService;
    @Autowired
    private OrderUtils orderUtils;
    @Autowired
    private PaySuccessSourceFactory paySuccessSourceFactory;
    /**
     * 支付成功处理
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean onPaySuccess(String tradeNo, Integer payStatus, Integer payType, PayDataVo payData){
        Order order = this.getPayDetail(tradeNo, payStatus);
        if (order == null) {
            log.info("未找到该订单信息, tradeNo={}", tradeNo);
            return false;
        }
        // 更新订单状态
        this.updateOrderInfo(order, payType, payData);
        // 累积用户总消费金额
        this.setIncPayMoney(order.getUserId(), order.getPayPrice());
        // 记录订单支付信息
        this.updatePayInfo(order);
        // 订单支付成功行为,异步执行
        paySuccessSourceFactory.getFactory(order.getOrderSource()).onPaySuccess(order.getOrderId());
        return true;
    }

    /**
     * 支付成功订单信息
     */
    public PaySuccessOrder getOrder(String tradeNo){
        Order order = this.getPayDetail(tradeNo, 0);
        PaySuccessOrder paySuccessOrder = new PaySuccessOrder();
        paySuccessOrder.setPayStatus(order.getPayStatus());
        paySuccessOrder.setAppId(order.getAppId());
        return paySuccessOrder;
    }

    /**
     * 获取订单
     * @return
     */
    private Order getPayDetail(String tradeNo, Integer payStatus){
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getTradeNo, tradeNo);
        if(payStatus > 0){
            wrapper.eq(Order::getPayStatus, payStatus);
        }
        return orderService.getOne(wrapper);
    }

    /**
     * 更新订单记录
     */
    private void updateOrderInfo(Order order, Integer payType, PayDataVo payData)
    {
        // 更新商品库存、销量
        productFactory.getFactory(order.getOrderSource()).updateProductStockSales(orderUtils.getOrderProduct(order.getOrderId()));
        // 整理订单信息
        String paySource = "";
        if (payData.getAttach() != null) {
            paySource = payData.getAttach().getString("paySource");
        }
        Order updateOrder = new Order();
        updateOrder.setOrderId(order.getOrderId());
        updateOrder.setPayType(payType);
        updateOrder.setPayStatus(20);
        updateOrder.setPayTime(new Date());
        updateOrder.setPaySource(paySource);
        if (payType == OrderPayTypeEnum.WECHAT.getValue()) {
            updateOrder.setTransactionId(payData.getTransaction_id());
        }
        // 更新订单状态
        orderService.updateById(updateOrder);
    }

    /**
     * 累计用户消费金额
     * @param userId
     * @param payPrice
     */
    private void setIncPayMoney(Integer userId, BigDecimal payPrice){
        userService.update(new LambdaUpdateWrapper<User>().eq(User::getUserId, userId)
                .setSql("`pay_money` = `pay_money` + " + payPrice));
    }

    /**
     * 记录支付信息
     */
    private void updatePayInfo(Order order){
        // 余额支付
        if(order.getBalance().compareTo(BigDecimal.ZERO) > 0){
            // 更新用户余额
            userService.update(new LambdaUpdateWrapper<User>().eq(User::getUserId, order.getUserId())
                    .setSql("`balance` = `balance` - " + order.getBalance()));
            // 记录日志
            UserBalanceLog log = new UserBalanceLog();
            log.setUserId(order.getUserId());
            log.setMoney(order.getBalance().negate());
            log.setScene(BalanceLogEnum.CONSUME.getValue());
            log.setDescription(String.format("用户消费：%s", order.getOrderNo()));
            userBalanceLogService.save(log);
        }
    }
}
