package net.jjjshop.shop.service.app;

import net.jjjshop.common.entity.app.AppOpen;
import net.jjjshop.framework.common.service.BaseService;

/**
 * app应用记录表 服务类
 * @author jjjshop
 * @since 2022-07-27
 */
public interface AppOpenService extends BaseService<AppOpen> {

    /**
     * 修改
     * @param openappId
     * @param openappSecret
     * @return
     */
    Boolean edit(String openappId, String openappSecret, String logo);
}
