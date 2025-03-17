package net.jjjshop.front.service.user.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.settings.Express;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.DeliveryTypeEnum;
import net.jjjshop.common.enums.OrderPayStatusEnum;
import net.jjjshop.common.enums.OrderPayTypeEnum;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.settings.vo.TradeVo;
import net.jjjshop.common.util.KuaiDi100Utils;
import net.jjjshop.common.util.OrderUtils;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.front.service.order.OrderAddressService;
import net.jjjshop.front.service.order.OrderProductService;
import net.jjjshop.front.service.order.OrderService;
import net.jjjshop.front.service.settings.ExpressService;
import net.jjjshop.front.service.store.StoreService;
import net.jjjshop.front.service.user.UserOrderService;
import net.jjjshop.front.vo.order.OrderDetailVo;
import net.jjjshop.front.vo.settings.ExpressDetailVo;
import net.jjjshop.front.vo.settings.ExpressListVo;
import net.jjjshop.front.vo.store.ExtractStoreVo;
import net.jjjshop.front.vo.store.StoreVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * 用户订单 服务实现类
 * @author jjjshop
 * @since 2022-08-02
 */

@Slf4j
@Service
public class UserOrderServiceImpl implements UserOrderService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderProductService orderProductService;
    @Autowired
    private OrderAddressService orderAddressService;
    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private ExpressService expressService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private KuaiDi100Utils kuaiDi100Utils;

    /**
     * 获取订单详情
     * @param user
     * @param orderId
     * @return
     */
    public OrderDetailVo detail(User user, Integer orderId) {
        Order order = orderService.getUserOrderDetail(orderId, user.getUserId());
        //剩余支付时间
        String time = "";
        if (order.getPayStatus() == 10 && order.getOrderStatus() != 20 && order.getPayEndTime() != null) {
            time = this.formatPayEndTime(order.getPayEndTime().getTime() - new Date().getTime());
        }
        OrderDetailVo vo = this.transOrderDetailVo(order);
        //获取订单商品信息
        vo.setProduct(orderProductService.getProductVoList(orderId));
        //设置剩余支付时间
        vo.setPayEndTimeText(time);
        return vo;
    }

    /**
     * 获取物流信息详情
     * @param orderId
     * @return
     */
    public ExpressDetailVo express(Integer orderId) throws Exception {
        Order order = orderService.getById(orderId);
        Express express = expressService.getById(order.getExpressId());
        ExpressDetailVo vo = new ExpressDetailVo();
        BeanUtils.copyProperties(express, vo);
        vo.setExpressNo(order.getExpressNo());
        vo.setExpress(kuaiDi100Utils.query(express.getExpressCode(), vo.getExpressNo()));
        return vo;
    }

    /**
     * 转换订单详情VO
     * @param order
     * @param
     */
    public OrderDetailVo transOrderDetailVo(Order order) {
        OrderDetailVo vo = new OrderDetailVo();
        BeanUtils.copyProperties(order, vo);
        //订单支付种类文本
        vo.setPayTypeText(OrderPayTypeEnum.getName(vo.getPayType()));
        //订单支付状态文本
        vo.setPayStatusText(OrderPayStatusEnum.getName(vo.getPayStatus()));
        //物流种类文本
        vo.setDeliveryTypeText(DeliveryTypeEnum.getName(vo.getDeliveryType()));
        //物流状态文本
        if (vo.getDeliveryStatus() == 10) {
            vo.setDeliveryStatusText("未发货");
        } else {
            vo.setDeliveryStatusText("已发货");
        }
        if (vo.getReceiptStatus() == 10) {
            vo.setReceiptStatusText("未收货");
        } else {
            vo.setReceiptStatusText("已收货");
        }
        //设置后台修改价格
        if (vo.getUpdatePrice().compareTo(BigDecimal.ZERO) > 0) {
            vo.setUpdatePriceSymbol("+");
        } else if (vo.getUpdatePrice().compareTo(BigDecimal.ZERO) < 0) {
            vo.setUpdatePriceSymbol("-");
        } else {
            vo.setUpdatePriceSymbol("");
        }
        if (vo.getDeliveryType() == DeliveryTypeEnum.EXPRESS.getValue()) {
            //设置收货地址和物流信息
            vo.setAddress(orderAddressService.getOrderAddress(order.getOrderId()));
            if (order.getExpressId() != null && order.getExpressId() > 0) {
                Express express = expressService.getById(order.getExpressId());
                ExpressListVo expressVo = new ExpressListVo();
                BeanUtils.copyProperties(express, expressVo);
                vo.setExpress(expressVo);
            }
            //设置自提门店信息
        } else if (vo.getDeliveryType() == DeliveryTypeEnum.EXTRACT.getValue()) {
            ExtractStoreVo extractStoreVo = new ExtractStoreVo();
            StoreVo storeVo = storeService.getStoreVoById(order.getExtractStoreId());
            if (storeVo != null) {
                BeanUtils.copyProperties(storeVo, extractStoreVo);
                vo.setExtractStore(extractStoreVo);
            }
        }
        //设置订单状态文本
        vo.setOrderStatusText(OrderUtils.getOrderStatusText(order));
        //设置订单状态文本
        vo.setStateText(this.getStateTextAttr(order));
        vo.setIsAllowRefund(this.isAllowRefund(order));
        return vo;
    }

    /**
     * 获取订单剩余支付时间
     * @param leftTime
     * @return
     */
    private String formatPayEndTime(Long leftTime) {
        String str = "";
        DecimalFormat df = new DecimalFormat("#");
        if (leftTime <= 0) {
            return "";
        }
        leftTime = leftTime / 1000;
        Double day = Math.floor(leftTime / 86400);
        Double hour = Math.floor((leftTime - day * 86400) / 3600);
        Double min = Math.floor((leftTime - day * 86400 - hour * 3600) / 60);
        if (day > 0) {
            str = str + df.format(day) + "天";
        }
        if (hour > 0) {
            str = str + df.format(hour) + "小时";
        }
        if(min == 0){
            //由于定时任务为每分钟执行，不足一分钟时显示为一分钟
            min = 1.0;
        }
        if (min > 0) {
            str = str + df.format(min) + "分钟";
        }
        return str;
    }

    /**
     * 订单状态文字描述
     * @param order
     * @return
     */
    private String getStateTextAttr(Order order) {
        if (order.getOrderStatus() == 20) {
            return "已取消";
        }
        if (order.getOrderStatus() == 30) {
            return "已完成";
        }
        if (order.getPayStatus() == 10) {
            return "待付款";
        }
        if (order.getDeliveryStatus() == 10) {
            return "已付款,待发货";
        }
        if (order.getDeliveryStatus() == 20) {
            return "已发货,待收货";
        }
        return "";
    }

    /**
     * 检查订单是否允许申请售后
     * @param order
     * @return
     */
    private Boolean isAllowRefund(Order order) {
        //必须是已经发货的订单
        if (order.getDeliveryStatus() != 20) {
            return false;
        }
        JSONObject jsonObject = settingUtils.getSetting(SettingEnum.TRADE.getKey(), null);
        TradeVo tradeVo = jsonObject.toJavaObject(TradeVo.class);
        if (tradeVo.getRefundDays() == 0) {
            return false;
        }
        if (order.getReceiptStatus() == 20
                && DateUtil.offsetDay(order.getReceiptTime(), +tradeVo.getRefundDays()).isBefore(new Date())) {
            return false;
        }
        return true;
    }

}
