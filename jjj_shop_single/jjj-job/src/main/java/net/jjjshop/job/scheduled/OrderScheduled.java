

package net.jjjshop.job.scheduled;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.app.App;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.factory.product.ProductFactory;
import net.jjjshop.common.service.app.AppService;
import net.jjjshop.common.settings.vo.TradeVo;
import net.jjjshop.common.util.OrderUtils;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.common.util.UserCouponUtils;
import net.jjjshop.common.util.UserUtils;
import net.jjjshop.job.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单定时任务、未付款关闭，自动确认收货，订单结算
 */

@Slf4j
@Component
public class OrderScheduled {
    @Autowired
    private OrderService orderService;
    @Autowired
    private AppService appService;
    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private ProductFactory productFactory;
    @Autowired
    private OrderUtils orderUtils;
    @Autowired
    private UserCouponUtils userCouponUtils;
    @Autowired
    private UserUtils userUtils;
    /**
     * 每分钟执行一次
     */
    @Scheduled(cron = "0 */1 * * * ? ")
    public void execute() throws Exception {
        // 普通订单行为管理
        this.master();
        // 未支付订单自动关闭，拼团,预售除外
        this.close();
    }

    /**
     * 普通订单行为管理
     */
    private void master(){
        // 获取所有应用
        List<App> appList = appService.list(new LambdaQueryWrapper<App>().eq(App::getIsDelete, 0));
        for(App app:appList){
            // 获取商城交易设置
            JSONObject vo = settingUtils.getSetting(SettingEnum.TRADE.getKey(), Long.valueOf(app.getAppId()));
            TradeVo tradeVo = JSONObject.toJavaObject(vo, TradeVo.class);
            // 已发货订单自动确认收货
            this.receive(tradeVo.getReceiveDays(), app.getAppId());
            // 已完成订单结算
            this.settled(tradeVo.getRefundDays(), app.getAppId());
        }
    }

    /**
     * 已发货订单自动确认收货
     */
    private void receive(Integer receiveDays, Integer appId){
        // 截止时间
        if (receiveDays < 1) return;
        Date deadlineTime = DateUtil.offsetDay(new Date(), -receiveDays);
        // 条件
        List<Order> orderList = orderService.list(new LambdaQueryWrapper<Order>().eq(Order::getDeliveryStatus, 20)
                .eq(Order::getReceiptStatus, 10).eq(Order::getAppId, appId)
                .le(Order::getDeliveryTime, deadlineTime));
        // 订单id集
        List<Integer> orderIds = orderList.stream().map(Order::getOrderId).collect(Collectors.toList());

        if (orderIds.size() > 0) {
            // 更新订单收货状态
            orderService.update(new LambdaUpdateWrapper<Order>().in(Order::getOrderId, orderIds)
                    .set(Order::getReceiptStatus, 20).set(Order::getOrderStatus, 30).set(Order::getReceiptTime, new Date()));
            // 批量处理已完成的订单
            orderUtils.complete(orderList, appId);
        }
        // 记录日志
        if(orderIds.size() > 0){
            log.info(String.format("order scheduled receive receiveDays:%s deadlineTime:%s orderIds:%s", receiveDays, deadlineTime, orderIds));
        }
    }

    /**
     * 已完成订单结算
     */
    private void settled(Integer refundDays, Integer appId)
    {
        // 获取已完成的订单（未累积用户实际消费金额）
        // 条件1：订单状态：已完成
        // 条件2：超出售后期限
        // 条件3：is_settled 为 0
        // 截止时间
        Date deadlineTime = DateUtil.offsetDay(new Date(), -refundDays);
        // 查询订单列表
        List<Order> orderList = orderService.list(new LambdaQueryWrapper<Order>()
                //订单状态10=>进行中，20=>已经取消，30=>已完成
                .eq(Order::getOrderStatus, 30)
                //订单是否已结算(0未结算 1已结算)
                .eq(Order::getIsSettled, 0)
                .eq(Order::getAppId, appId)
                .le(Order::getReceiptTime, deadlineTime));
        // 订单id集
        List<Integer> orderIds = orderList.stream().map(Order::getOrderId).collect(Collectors.toList());
        if(orderList.size() > 0){
            // 订单结算服务
            orderUtils.settled(orderList);
        }
        // 记录日志
        if(orderIds.size() > 0){
            log.info(String.format("order scheduled settled refundDays:%s deadlineTime:%s orderIds:%s", refundDays, deadlineTime, orderIds));
        }
    }

    /**
     * 未支付订单自动关闭
     */
    private void close()
    {
        // 查询截止时间未支付的订单
        List<Order> items = orderService.list(new LambdaQueryWrapper<Order>().eq(Order::getPayStatus, 10)
                .eq(Order::getOrderStatus, 10)
                .eq(Order::getIsDelete, 0)
                .ne(Order::getOrderSource, 80)
                .isNotNull(Order::getPayEndTime)
                .lt(Order::getPayEndTime, new Date()));
        // 订单id集
        List<Integer> orderIds = items.stream().map(Order::getOrderId).collect(Collectors.toList());
        // 取消订单事件
        if (orderIds.size() > 0) {
            for (Order order:items) {
                // 回退商品库存
                productFactory.getFactory(order.getOrderSource()).backProductStock(orderUtils.getOrderProduct(order.getOrderId()), false);
                // 回退用户优惠券
                if(order.getCouponId() > 0) {
                    userCouponUtils.setIsUse(order.getCouponId(), 0);
                }
            }
            // 批量更新订单状态为已取消
            orderService.update(new LambdaUpdateWrapper<Order>().in(Order::getOrderId, orderIds).set(Order::getOrderStatus, 20));
        }
        // 记录日志
        if(orderIds.size() > 0){
            log.info(String.format("order scheduled close orderIds:%s", orderIds));
        }
    }
}
