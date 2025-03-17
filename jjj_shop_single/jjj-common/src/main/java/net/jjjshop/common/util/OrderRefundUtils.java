package net.jjjshop.common.util;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundV3Result;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.app.App;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.entity.user.UserBalanceLog;
import net.jjjshop.common.enums.BalanceLogEnum;
import net.jjjshop.common.enums.OrderPayTypeEnum;
import net.jjjshop.common.service.app.AppService;
import net.jjjshop.common.service.user.UserBalanceLogService;
import net.jjjshop.common.service.user.UserService;
import net.jjjshop.common.util.wx.WxPayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class OrderRefundUtils {
    @Autowired
    private WxPayUtils wxPayUtils;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserBalanceLogService userBalanceLogService;
    @Autowired
    private AppService appService;
    /**
     * 执行订单退款
     */
    public Boolean execute(Order order, BigDecimal money)
    {
        // 退款金额，如不指定则默认为订单实付款金额
        if(money == null){
            money = order.getPayPrice();
        }
        int payType = order.getPayType();
        if (payType != OrderPayTypeEnum.BALANCE.getValue().intValue()
                && order.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            if (order.getRefundMoney().compareTo(order.getBalance()) < 0) {
                if(order.getRefundMoney().add(money).compareTo(order.getBalance()) > 0){
                    BigDecimal balance = order.getBalance().subtract(order.getRefundMoney());
                    money = money.subtract(balance);
                    this.balance(order, balance);
                }else{
                    payType = 10;
                }
            }
        }
        // 1.微信支付退款
        if (payType == OrderPayTypeEnum.WECHAT.getValue()) {
            return this.wxpay(order, money);
        }
        // 2.余额支付退款
        if (payType == OrderPayTypeEnum.BALANCE.getValue()) {
            return this.balance(order, money);
        }
        // 3.支付宝退款
        /*if ($pay_type == OrderPayTypeEnum::ALIPAY) {
            return $this->alipay($order, $money);
        }*/
        return false;
    }

    /**
     * 微信支付退款
     */
    private Boolean wxpay(Order order, BigDecimal money)
    {
        App app = appService.getById(order.getAppId());
        if(app.getWxPayKind() == 2){
            return this.refundV2(order, money);
        }else{
            return this.refundV3(order, money);
        }

    }

    private boolean refundV2(Order order, BigDecimal money){
        try{
            this.wxPayService.setConfig(wxPayUtils.getConfig(order.getPaySource(), Long.valueOf(order.getAppId())));
            WxPayRefundRequest request = new WxPayRefundRequest();
            request.setTransactionId(order.getTransactionId());
            request.setTotalFee(order.getOnlineMoney().multiply(new BigDecimal(100)).intValue());
            request.setRefundFee(money.multiply(new BigDecimal(100)).intValue());
            request.setOutRefundNo(OrderUtils.geneOrderNo(order.getUserId()));
            request.setRefundDesc("用户申请取消");
            WxPayRefundResult result = this.wxPayService.refund(request);
            log.info("退款结果:{}:{}",result.getReturnCode(),result.getResultCode());
            if("FAIL".equals(result.getReturnCode())){
                log.info("退款失败return:{}",result.getReturnMsg());
                return false;
            }
            if("FAIL".equals(result.getResultCode())){
                log.info("退款失败result:{}",result.getResultCode());
                return false;
            }
            return true;
        }catch (Exception e){
            log.info("微信退款失败,{}", e.getMessage());
            return false;
        }
    }

    private boolean refundV3(Order order, BigDecimal money){
        try{
            this.wxPayService.setConfig(wxPayUtils.getConfig(order.getPaySource(), Long.valueOf(order.getAppId())));
            WxPayRefundV3Request request = new WxPayRefundV3Request();
            request.setTransactionId(order.getTransactionId());
            request.setOutRefundNo(OrderUtils.geneOrderNo(order.getUserId()));
            // 订单总金额
            BigDecimal payPrice = order.getOnlineMoney();
            WxPayRefundV3Request.Amount am = new WxPayRefundV3Request.Amount();
            am.setTotal(payPrice.multiply(new BigDecimal(100)).intValue());
            am.setRefund(money.multiply(new BigDecimal(100)).intValue());
            am.setCurrency("CNY");
            request.setAmount(am);
            request.setReason("用户申请取消");
            WxPayRefundV3Result result = this.wxPayService.refundV3(request);
            log.info("退款结果:{}", result.getStatus());
            if("CLOSED".equals(result.getStatus()) || "ABNORMAL".equals(result.getStatus())){
                log.info("退款失败:{}", order.getOrderNo());
                return false;
            }
            return true;
        }catch (Exception e){
            log.info("微信退款失败,{}", e.getMessage());
            return false;
        }
    }

    /**
     * 余额支付退款
     */
    private boolean balance(Order order, BigDecimal money)
    {
        // 回退用户余额
        userService.update(new LambdaUpdateWrapper<User>().eq(User::getUserId, order.getUserId())
                .setSql("`balance` = `balance` + " + money));
        // 记录日志
        UserBalanceLog log = new UserBalanceLog();
        log.setUserId(order.getUserId());
        log.setMoney(money);
        log.setScene(BalanceLogEnum.REFUND.getValue());
        log.setDescription(String.format("用户退款：%s", order.getOrderNo()));
        log.setAppId(order.getAppId());
        userBalanceLogService.save(log);
        return true;
    }
}
