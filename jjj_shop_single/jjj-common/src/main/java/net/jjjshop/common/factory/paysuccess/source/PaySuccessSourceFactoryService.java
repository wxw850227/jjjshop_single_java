package net.jjjshop.common.factory.paysuccess.source;

import net.jjjshop.common.entity.order.Order;

/**
 * 支付成功工厂接口类
 */
public abstract class PaySuccessSourceFactoryService {
    /**
     * 订单公共业务
     */
    protected void onCommonEvent(Order order)
    {
        // 发送消息通知
        //(new MessageService)->payment($this->order, $this->orderType);
        System.out.println("onCommonEvent onCommonEvent");
    }

    /**
     * 支付成功处理
     */
    public abstract void onPaySuccess(Integer orderId);
}
