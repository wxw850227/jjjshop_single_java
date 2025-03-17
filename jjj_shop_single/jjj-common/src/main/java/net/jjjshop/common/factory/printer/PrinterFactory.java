package net.jjjshop.common.factory.printer;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.order.OrderAddress;
import net.jjjshop.common.entity.order.OrderExtract;
import net.jjjshop.common.entity.order.OrderProduct;
import net.jjjshop.common.entity.settings.Printer;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.DeliveryTypeEnum;
import net.jjjshop.common.enums.PrinterTypeEnum;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.service.order.OrderAddressService;
import net.jjjshop.common.service.order.OrderExtractService;
import net.jjjshop.common.service.order.OrderProductService;
import net.jjjshop.common.service.settings.PrinterService;
import net.jjjshop.common.service.store.StoreService;
import net.jjjshop.common.service.user.UserService;
import net.jjjshop.common.settings.vo.PrinterVo;
import net.jjjshop.common.settings.vo.StoreVo;
import net.jjjshop.common.util.SettingUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 文件上传工厂
 */
@Component
public class PrinterFactory {

    private static final String packageName = "net.jjjshop.common.factory.printer.impl.%sPrinterFactoryService";

    @Autowired
    private Map<String, PrinterFactoryService> map;//关键在这个，原理：当一个接口有多个实现类的时候，key为实现类名，value为实现类对象

    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private PrinterService printerService;
    @Autowired
    private OrderAddressService orderAddressService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderExtractService orderExtractService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private OrderProductService orderProductService;


    public PrinterFactoryService getService(String printerType) {
        String className = String.format(packageName, printerType);
        return map.get(className);
    }

    /**
     * 打印
     * @param order
     */
    public void print(Order order){
        JSONObject vo = settingUtils.getSetting(SettingEnum.PRINTING.getKey(), Long.valueOf(order.getAppId()));
        PrinterVo printerVo = JSONObject.toJavaObject(vo, PrinterVo.class);
        if(printerVo.getIsOpen() == 0 || printerVo.getPrinterId() == 0 || printerVo.getOrderStatus() != 20){
            return;
        }
        Printer printer = printerService.getById(printerVo.getPrinterId());
        if(printer == null || printer.getIsDelete() == 1){
            return;
        }
        // 获取订单打印内容
        String content = this.getPrintContent(order);
        this.getService(PrinterTypeEnum.getClassName(printer.getPrinterType())).printTicket(printer, content);
    }

    /**
     * 构建订单打印的内容
     */
    private String getPrintContent(Order order)
    {
        JSONObject storeJson = settingUtils.getSetting("store", Long.valueOf(order.getAppId()));
        StoreVo storeVo = storeJson.toJavaObject(StoreVo.class);
        String storeName = storeVo.getName();
        OrderAddress orderAddress = orderAddressService.getOne(new LambdaQueryWrapper<OrderAddress>().eq(OrderAddress::getOrderId, order.getOrderId()));
        String fullAddress = orderAddressService.getFullAddress(orderAddress);
        User user = userService.getById(order.getUserId());
        OrderExtract extract = orderExtractService.getOne(new LambdaQueryWrapper<OrderExtract>().eq(OrderExtract::getOrderId, order.getOrderId()));
        List<OrderProduct> orderProducts = orderProductService.list(new LambdaQueryWrapper<OrderProduct>().eq(OrderProduct::getOrderId, order.getOrderId()));

        StringBuilder content = new StringBuilder();
        content.append("<CB>").append(storeName).append("</CB><BR>");
        content.append("--------------------------------<BR>");
        content.append("昵称").append(user.getNickname()).append(user.getUserId()).append("<BR>");
        content.append("订单号：").append(order.getOrderNo()).append("<BR>");
        content.append("付款时间：").append(DateUtil.format(order.getPayTime(),"YYYY-MM-dd HH:mm:ss")).append("<BR>");
        if(order.getDeliveryType() == DeliveryTypeEnum.EXPRESS.getValue()){
            content.append("--------------------------------<BR>");
            content.append("收货人：").append(orderAddress.getName()).append("<BR>");
            content.append("联系电话：").append(orderAddress.getPhone()).append("<BR>");
            content.append("收货地址：").append(fullAddress).append("<BR>");
        }
        if(order.getDeliveryType() == DeliveryTypeEnum.EXTRACT.getValue() && extract != null){
            content.append("--------------------------------<BR>");
            content.append("联系人：").append(extract.getLinkman()).append("<BR>");
            content.append("联系电话：").append(extract.getPhone()).append("<BR>");
            content.append("自提门店：").append(storeService.getById(order.getExtractStoreId()).getStoreName()).append("<BR>");
        }
        content.append("=========== 商品信息 ===========<BR>");
        for(OrderProduct orderProduct : orderProducts){
            content.append("商品名称：").append(orderProduct.getProductName()).append("<BR>");
            if(StringUtils.isNotEmpty(orderProduct.getProductAttr())){
                content.append("商品规格：").append(orderProduct.getProductAttr()).append("<BR>");
            }
            content.append("购买数量：").append(orderProduct.getTotalNum()).append("<BR>");
            content.append("商品总价：").append(orderProduct.getTotalPrice()).append("<BR>");
            content.append("--------------------------------<BR>");
        }
        if(StringUtils.isNotEmpty(order.getBuyerRemark())){
            content.append("=========== 买家备注 ===========<BR>");
            content.append("<B>").append(order.getBuyerRemark()).append("</B><BR>");
            content.append("--------------------------------<BR>");
        }
        if(order.getCouponMoney().compareTo(BigDecimal.ZERO) > 0){
            content.append("优惠券：").append(order.getCouponMoney()).append("元<BR>");
        }
        if(order.getUpdatePrice().compareTo(BigDecimal.ZERO) > 0){
            content.append("后台改价：").append(order.getUpdatePrice().compareTo(BigDecimal.ZERO) > 0 ? "+":"-").append(order.getUpdatePrice()).append("<BR>");
        }
        if(order.getDeliveryType().intValue() == DeliveryTypeEnum.EXPRESS.getValue().intValue()){
            content.append("运费：").append(order.getExpressPrice()).append("元<BR>");
            content.append("--------------------------------<BR>");
        }
        content.append("<RIGHT>实付款：<BOLD><B>").append(order.getPayPrice()).append("</B></BOLD>元</RIGHT><BR>");
        return content.toString();
    }
}
