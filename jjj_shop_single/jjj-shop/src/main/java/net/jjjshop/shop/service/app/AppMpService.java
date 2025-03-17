package net.jjjshop.shop.service.app;

import net.jjjshop.common.entity.app.AppMp;
import net.jjjshop.framework.common.service.BaseService;

/**
 * 微信公众号记录表 服务类
 * @author jjjshop
 * @since 2022-07-27
 */
public interface AppMpService extends BaseService<AppMp> {

    /**
     * 修改
     * @param mpappId
     * @param mpappSecret
     * @return
     */
    Boolean edit(String mpappId, String mpappSecret);
}
