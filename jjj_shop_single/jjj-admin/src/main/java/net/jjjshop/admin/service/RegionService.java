package net.jjjshop.admin.service;

import net.jjjshop.admin.param.RegionPageParam;
import net.jjjshop.admin.param.RegionParam;
import net.jjjshop.admin.vo.RegionVo;
import net.jjjshop.common.entity.settings.Region;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;

/**
 *  服务类
 *
 * @author jjjshop
 * @since 2022-06-24
 */
public interface RegionService extends BaseService<Region> {
    /**
     * 地区列表
     * @param regionPageParam
     * @return
     */
    Paging<Region> getList(RegionPageParam regionPageParam);
    /**
     * 删除
     * @param id
     * @return
     */
    Boolean setDelete(Integer id);
    /**
     * 新增
     * @param regionParam
     * @return
     */
    Boolean add(RegionParam regionParam);

    /**
     * 获取编辑数据
     * @param id
     * @return
     */
    RegionVo getEditData(Integer id);
    /**
     * 修改
     * @param regionParam
     * @return
     */
    Boolean edit(RegionParam regionParam);
}
