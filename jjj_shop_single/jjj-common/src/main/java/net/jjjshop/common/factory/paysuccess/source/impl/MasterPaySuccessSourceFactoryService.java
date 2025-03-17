package net.jjjshop.common.factory.paysuccess.source.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.order.OrderProduct;
import net.jjjshop.common.enums.DeliveryTypeEnum;
import net.jjjshop.common.factory.paysuccess.source.PaySuccessSourceFactoryService;
import net.jjjshop.common.factory.printer.PrinterFactory;
import net.jjjshop.common.service.order.OrderProductService;
import net.jjjshop.common.service.order.OrderService;
import net.jjjshop.common.util.OrderUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 支付成功
 */
@Slf4j
@Service
public class MasterPaySuccessSourceFactoryService extends PaySuccessSourceFactoryService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderUtils orderUtils;
    @Autowired
    private PrinterFactory printerFactory;
    @Autowired
    private OrderProductService orderProductService;
    /**
     * 支付成功处理
     */
    @Async
    public void onPaySuccess(Integer orderId){
        Order order = orderService.getById(orderId);
        // 如果是虚拟商品，则标记为已完成，无需发货
        if(order.getDeliveryType().intValue() == DeliveryTypeEnum.NO_EXPRESS.getValue() && order.getVirtualAuto() == 1) {
            Order updatOrder = new Order();
            Date now = new Date();
            updatOrder.setOrderId(order.getOrderId());
            updatOrder.setDeliveryStatus(20);
            updatOrder.setDeliveryTime(now);
            updatOrder.setReceiptStatus(20);
            updatOrder.setReceiptTime(now);
            updatOrder.setOrderStatus(30);
            List<OrderProduct> list = orderProductService.list(new LambdaQueryWrapper<OrderProduct>().eq(OrderProduct::getOrderId, order.getOrderId()));
            if(CollectionUtils.isNotEmpty(list)){
                updatOrder.setVirtualContent(list.get(0).getVirtualContent());
            }
            orderService.updateById(updatOrder);
            //执行订单完成后操作
            List<Order> orderList = new ArrayList<>();
            orderList.add(orderService.getById(orderId));
            orderUtils.complete(orderList, order.getAppId());
        }
        // 公共事件
        this.onCommonEvent(order);
        // 小票打印
        printerFactory.print(order);
    }
}
