package net.jjjshop.common.util;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.app.App;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.OrderTypeEnum;
import net.jjjshop.common.service.app.AppService;
import net.jjjshop.common.util.wx.WxPayUtils;
import net.jjjshop.config.properties.SpringBootJjjProperties;
import net.jjjshop.framework.core.bean.RequestDetail;
import net.jjjshop.framework.core.util.RequestDetailThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Map;

@Slf4j
@Component
public class PayUtils {

    @Autowired
    private SpringBootJjjProperties springBootJjjProperties;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private WxPayUtils wxPayUtils;
    @Autowired
    private AppService appService;

    /**
     * 微信支付
     * @return
     */
    public Object onPaymentByWechat(User user, String orderNo, String tradeNo, BigDecimal onlineMoney,
                                     String paySource) throws Exception{
        App app = appService.getById(user.getAppId());
        if(app.getWxPayKind() == 2){
            return this.onPaymentByWechatV2(user, orderNo, tradeNo, onlineMoney,
                    paySource);
        }else {
            return this.onPaymentByWechatV3(user, tradeNo, onlineMoney,
                    paySource);
        }
    }

    public Object onPaymentByWechatV2(User user, String orderNo, String tradeNo, BigDecimal onlineMoney,
                                    String paySource) throws Exception{
        WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
        request.setBody(orderNo);
        request.setOutTradeNo(tradeNo);
        request.setTotalFee(onlineMoney.multiply(new BigDecimal(100)).intValue());
        RequestDetail requestDetail = RequestDetailThreadLocal.getRequestDetail();
        request.setSpbillCreateIp(requestDetail.getIp());
        request.setNotifyUrl(springBootJjjProperties.getServerIp() + "/api/job/notify/wxPay");
        request.setTradeType("JSAPI");
        request.setOpenid(user.getOpenId());
        JSONObject attach = new JSONObject();
        attach.put("orderType", OrderTypeEnum.MASTER.getValue());
        attach.put("paySource", paySource);
        request.setAttach(attach.toJSONString());
        // 先设置，再调用
        this.wxPayService.setConfig(wxPayUtils.getConfig(paySource, null));
        return this.wxPayService.createOrder(request);
    }


    public Object onPaymentByWechatV3(User user, String tradeNo, BigDecimal onlineMoney,
                                      String paySource) throws Exception{
        WxPayUnifiedOrderV3Request request = new WxPayUnifiedOrderV3Request();
        request.setOutTradeNo(tradeNo);
        WxPayUnifiedOrderV3Request.Amount am = new WxPayUnifiedOrderV3Request.Amount();
        am.setTotal(onlineMoney.multiply(new BigDecimal(100)).intValue());
        am.setCurrency("CNY");
        request.setAmount(am);
        RequestDetail requestDetail = RequestDetailThreadLocal.getRequestDetail();
        request.setNotifyUrl(springBootJjjProperties.getServerIp() + "/api/job/notify/wxPay");
        request.setDescription(tradeNo);

        WxPayUnifiedOrderV3Request.SceneInfo scen = new WxPayUnifiedOrderV3Request.SceneInfo();
        scen.setPayerClientIp(requestDetail.getIp());

        TradeTypeEnum tradeTypeEnum = null;
        WxPayUnifiedOrderV3Request.Payer payer = new WxPayUnifiedOrderV3Request.Payer();
        payer.setOpenid(user.getOpenId());
        tradeTypeEnum = TradeTypeEnum.JSAPI;
        request.setSceneInfo(scen);
        request.setPayer(payer);
        JSONObject attach = new JSONObject();
        attach.put("orderType", OrderTypeEnum.MASTER.getValue());
        attach.put("paySource", paySource);
        request.setAttach(attach.toJSONString());
        // 先设置，再调用
        this.wxPayService.setConfig(wxPayUtils.getConfig(paySource, null));
        return this.wxPayService.createOrderV3(tradeTypeEnum, request);
    }
}
