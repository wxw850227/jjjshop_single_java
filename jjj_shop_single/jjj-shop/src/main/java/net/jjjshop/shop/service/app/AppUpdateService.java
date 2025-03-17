package net.jjjshop.shop.service.app;

import net.jjjshop.common.entity.app.AppUpdate;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.CommonPageParam;
import net.jjjshop.shop.param.app.AppUpdateParam;

/**
 * app升级记录表 服务类
 * @author jjjshop
 * @since 2022-07-27
 */
public interface AppUpdateService extends BaseService<AppUpdate> {

    /**
     * 分页查询
     * @param commonPageParam
     * @return
     */
    Paging<AppUpdate> getList(CommonPageParam commonPageParam);

    /**
     * 添加
     * @param appUpdateParam
     * @return
     */
    boolean add(AppUpdateParam appUpdateParam);

    /**
     * 修改
     * @param appUpdateParam
     * @return
     */
    boolean edit(AppUpdateParam appUpdateParam);

    /**
     * 删除
     * @param id
     * @return
     */
    boolean delById(Integer id);
}
