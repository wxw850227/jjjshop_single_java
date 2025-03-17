package net.jjjshop.shop.service.app.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.app.AppMp;
import net.jjjshop.common.mapper.app.AppMpMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.shop.service.app.AppMpService;
import org.springframework.stereotype.Service;

/**
 * 微信公众号记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-27
 */
@Slf4j
@Service
public class AppMpServiceImpl extends BaseServiceImpl<AppMpMapper, AppMp> implements AppMpService {

    /**
     * 修改
     * @param mpappId
     * @param mpappSecret
     * @return
     */
    public Boolean edit(String mpappId, String mpappSecret) {
        AppMp app = this.getOne(new LambdaQueryWrapper<>());
        AppMp updateBean = new AppMp();
        updateBean.setMpappId(mpappId);
        updateBean.setMpappSecret(mpappSecret);
        if (app == null) {
            return this.save(updateBean);
        } else {
            return this.update(updateBean, new LambdaQueryWrapper<>());
        }
    }

}
