package net.jjjshop.shop.service.app.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.app.AppWx;
import net.jjjshop.common.mapper.app.AppWxMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.shop.service.app.AppWxService;
import org.springframework.stereotype.Service;

/**
 * 微信小程序记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-03
 */
@Slf4j
@Service
public class AppWxServiceImpl extends BaseServiceImpl<AppWxMapper, AppWx> implements AppWxService {

    /**
     * 修改
     * @param wxappId
     * @param wxappSecret
     * @return
     */
    public Boolean edit(String wxappId, String wxappSecret){
        AppWx app = this.getOne(new LambdaQueryWrapper<>());
        AppWx updateBean = new AppWx();
        updateBean.setWxappId(wxappId);
        updateBean.setWxappSecret(wxappSecret);
        if(app == null){
            return this.save(updateBean);
        }else{
            return this.update(updateBean, new LambdaQueryWrapper<>());
        }
    }
}
