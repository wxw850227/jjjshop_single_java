package net.jjjshop.common.util.wx;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.github.binarywang.wxpay.v3.auth.Verifier;
import net.jjjshop.common.entity.app.App;
import net.jjjshop.common.entity.app.AppMp;
import net.jjjshop.common.entity.app.AppOpen;
import net.jjjshop.common.entity.app.AppWx;
import net.jjjshop.common.service.app.AppMpService;
import net.jjjshop.common.service.app.AppOpenService;
import net.jjjshop.common.service.app.AppService;
import net.jjjshop.common.service.app.AppWxService;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.core.util.RequestDetailThreadLocal;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

/**
 * 维信小程序工具类
 */
@Configuration
public class WxPayUtils {

    @Lazy
    @Autowired
    private AppService appService;
    @Lazy
    @Autowired
    private AppWxService appWxService;
    @Lazy
    @Autowired
    private AppMpService appMpService;
    @Lazy
    @Autowired
    private AppOpenService appOpenService;

    @Bean
    public WxPayService wxPayService() {
        return new WxPayServiceImpl();
    }

    public WxPayConfig getConfig(String paySource, Long appId){
        if(appId == null){
            appId = RequestDetailThreadLocal.getRequestDetail().getAppId();
        }
        App app = appService.getById(appId);
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setMchId(StringUtils.trimToNull(app.getMchid()));
        payConfig.setMchKey(StringUtils.trimToNull(app.getApikey()));
        payConfig.setApiV3Key(StringUtils.trimToNull(app.getApikey()));
        payConfig.setSubAppId(null);
        payConfig.setSubMchId(null);
        payConfig.setKeyContent(app.getP12());
        payConfig.setPrivateKeyContent(app.getKeyPem().getBytes());
        payConfig.setPrivateCertContent(app.getCertPem().getBytes());
        // 可以指定是否使用沙箱环境
        payConfig.setUseSandboxEnv(false);
        if ("mp".equals(paySource) || "payH5".equals(paySource)) {
            AppMp appMp = appMpService.getById(appId);
            payConfig.setAppId(StringUtils.trimToNull(appMp.getMpappId()));
        } else if ("wx".equals(paySource)) {
            AppWx appWx = appWxService.getById(appId);
            if(appWx != null){
                payConfig.setAppId(StringUtils.trimToNull(appWx.getWxappId()));
            }
        } else if ("android".equals(paySource) || "ios".equals(paySource)) {
            AppOpen appOpen = appOpenService.getById(appId);
            payConfig.setAppId(StringUtils.trimToNull(appOpen.getOpenappId()));
        }
        if(app.getWxPayKind() == 3){
            if(StringUtils.isBlank(app.getWechatpaySerial())){
                throw new BusinessException("微信平台证书序列号不能为空");
            }
        }
        return payConfig;
    }
}
