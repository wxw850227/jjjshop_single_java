package net.jjjshop.common.util;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.order.OrderProduct;
import net.jjjshop.common.entity.order.OrderRefund;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.factory.product.vo.UpdateProductStockVo;
import net.jjjshop.common.service.order.OrderProductService;
import net.jjjshop.common.service.order.OrderRefundService;
import net.jjjshop.common.service.order.OrderService;
import net.jjjshop.common.service.user.UserService;
import net.jjjshop.common.settings.vo.TradeVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OrderUtils {
    @Autowired
    private OrderProductService orderProductService;
    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRefundService orderRefundService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserUtils userUtils;
    /**
     * 生成订单号
     * @return
     */
    public static String geneOrderNo(Integer userId){

        String date = DateUtil.format(new Date(), "yyyyMMdd");

        Random random = new Random();

        int rannum = (int) (random.nextDouble() * (9999 - 1000 + 1)) + 1000;// 获取4位随机数

        //8位用户id
        int subStrLength = 8;
        String sUserId = userId.toString();
        int length = sUserId.length();
        String str;
        if (length >= subStrLength) {
            str = sUserId.substring(length - subStrLength, length);
        } else {
            str = String.format("%0" + subStrLength + "d", userId);
        }

        return date + str + rannum;// 当前时间 + 用户id + 随机数
    }

    /**
     * 设置订单状态
     * @param order
     */
    public static String getOrderStatusText(Order order){
        if(order.getOrderStatus() == 20){
            return "已取消";
        }
        if(order.getOrderStatus() == 30){
            return "已完成";
        }
        if(order.getPayStatus() == 10){
            return "待付款";
        }
        // 发货状态
        if (order.getDeliveryStatus() == 10) {
            return "已付款，待发货";
        }
        if (order.getReceiptStatus() == 10) {
            return "已发货，待收货";
        }
        return "";
    }

    /**
     * 通过订单id，查询商品
     * @return
     */
    public List<UpdateProductStockVo> getOrderProduct(Integer orderId){
        List<OrderProduct> list = orderProductService.list(new LambdaQueryWrapper<OrderProduct>().eq(OrderProduct::getOrderId, orderId));
        // 转成vo
        return list.stream().map(e -> {
            UpdateProductStockVo vo = new UpdateProductStockVo();
            BeanUtils.copyProperties(e, vo);
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 订单完成
     */
    public void complete(List<Order> orderList, Integer appId){
        JSONObject vo = settingUtils.getSetting(SettingEnum.TRADE.getKey(), Long.valueOf(appId));
        TradeVo tradeVo = JSONObject.toJavaObject(vo, TradeVo.class);
        if(tradeVo.getRefundDays() == 0){
            this.settled(orderList);
        }
    }

    /**
     * 执行订单结算
     */
    public void settled(List<Order> orderList)
    {
        // 累积用户实际消费金额
        this.setIncUserData(orderList);
        // 订单id集
        List<Integer> orderIds = orderList.stream().map(Order::getOrderId).collect(Collectors.toList());
        // 将订单设置为已结算
        orderService.update(new LambdaUpdateWrapper<Order>().in(Order::getOrderId, orderIds)
                .set(Order::getIsSettled, 1));
    }

    /**
     * 累积用户实际消费金额/赠送积分
     */
    private void setIncUserData(List<Order> orderList)
    {
        // 累计消费金额
        Map<Integer,BigDecimal> expendMoneyData = new HashMap<>();
        HashSet<Integer> gradeUpUserIds = new HashSet<>();
        // 计算并累积实际消费金额(需减去售后退款的金额)
        for (Order order:orderList) {
            Integer userId = order.getUserId();
            gradeUpUserIds.add(userId);
            // 订单实际支付金额
            BigDecimal expendMoney = order.getPayPrice();
            List<OrderProduct> orderProductList = orderProductService.list(new LambdaQueryWrapper<OrderProduct>().eq(OrderProduct::getOrderId, order.getOrderId()));
            // 减去订单退款的金额
            for (OrderProduct product:orderProductList) {
                OrderRefund refund = orderRefundService.getOne(new LambdaQueryWrapper<OrderRefund>()
                        .eq(OrderRefund::getOrderId, order.getOrderId()).eq(OrderRefund::getOrderProductId, product.getOrderProductId()));
                // 售后类型：退货退款 // 商家审核：已同意
                if (refund != null && refund.getType() == 10 && refund.getIsAgree() == 10) {
                    expendMoney = expendMoney.subtract(refund.getRefundMoney());
                }
            }
            if(expendMoney.compareTo(BigDecimal.ZERO) > 0){
                if(expendMoneyData.get(order.getUserId()) == null){
                    expendMoneyData.put(order.getUserId(), expendMoney);
                }else{
                    expendMoneyData.put(order.getUserId(), expendMoneyData.get(order.getUserId()).add(expendMoney));
                }
            }
        }

        // 累积到会员消费金额
        expendMoneyData.forEach((key, value) -> {
            userService.update(new LambdaUpdateWrapper<User>().eq(User::getUserId, key)
                    .setSql("`expend_money` = `expend_money` + " + value));
        });
        //用户升级
        for (Integer id : gradeUpUserIds){
            userUtils.userGradeUpgrade(id);
        }
    }
}
