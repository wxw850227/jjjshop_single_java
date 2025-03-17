package net.jjjshop.shop.service.app.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.app.App;
import net.jjjshop.common.mapper.app.AppMapper;
import net.jjjshop.common.util.wx.WxPayUtils;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.util.ShopLoginUtil;
import net.jjjshop.shop.param.app.PayParam;
import net.jjjshop.shop.service.app.AppService;
import net.jjjshop.shop.vo.app.AppVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 微信小程序记录表 服务实现类
 * @author jjjshop
 * @since 2022-06-23
 */
@Slf4j
@Service
public class AppServiceImpl extends BaseServiceImpl<AppMapper, App> implements AppService {
    @Autowired
    private WxPayUtils wxPayUtils;
    /**
     * 修改app
     * @param passportType
     * @return
     */
    public Boolean edit(Integer passportType){
        // 先查询，admin端过期时间可以设置为null
        App app = this.detail();
        App updateBean = new App();
        updateBean.setPassportType(passportType);
        // 重新设置，防止更新成null
        updateBean.setExpireTime(app.getExpireTime());
        updateBean.setAppId(app.getAppId());
        return this.updateById(updateBean);
    }

    /**
     * 详情
     * @param
     * @return
     */
    public AppVo detail(){
        App app = this.getOne(new LambdaQueryWrapper<App>()
                .eq(App::getAppId, ShopLoginUtil.getLoginShopUserRedisVo().getAppId()));
        AppVo vo = new AppVo();
        BeanUtils.copyProperties(app, vo);
        vo.setPayTypeJson(JSON.parseObject(app.getPayType()));
        return vo;
    }

    /**
     * 修改支付设置
     * @param payParam
     * @return
     */
    public Boolean editPay(PayParam payParam){
        // 先查询，admin端过期时间可以设置为null
        App app = this.detail();
        App updateBean = new App();
        BeanUtils.copyProperties(payParam, updateBean);
        updateBean.setPayType(payParam.getPayType().toJSONString());
        updateBean.setExpireTime(app.getExpireTime());
        updateBean.setAppId(app.getAppId());
        this.updateById(updateBean);
        // v3支付获取平台序列号
        if(payParam.getWxPayKind() == 3){
            wxPayUtils.getConfig("wx", Long.valueOf(app.getAppId()));
        }
        return true;
    }
    /**
     * 保存p12证书
     * @param multipartFile
     * @throws Exception
     */
    public Boolean uploadP12(MultipartFile multipartFile){
        try{
            InputStream inputStream = multipartFile.getInputStream();
            byte[] p12 = null;
            p12 = new byte[inputStream.available()];
            inputStream.read(p12);
            inputStream.close();
            // 先查询，admin端过期时间可以设置为null
            App app = this.detail();
            App updateBean = new App();
            updateBean.setP12(p12);
            updateBean.setExpireTime(app.getExpireTime());
            updateBean.setAppId(app.getAppId());
            return this.updateById(updateBean);
        }catch (Exception e){
            log.info("p12证书上传失败{}",e.getMessage());
            return false;
        }
    }
}
