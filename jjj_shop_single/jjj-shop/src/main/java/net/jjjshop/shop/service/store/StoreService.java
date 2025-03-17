package net.jjjshop.shop.service.store;

import net.jjjshop.common.entity.store.Store;
import net.jjjshop.common.vo.RegionVo;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.CommonPageParam;
import net.jjjshop.shop.param.store.StoreParam;
import net.jjjshop.shop.vo.store.StoreVo;

import java.util.List;
import java.util.Map;

/**
 * 商家门店记录表 服务类
 * @author jjjshop
 * @since 2022-07-27
 */
public interface StoreService extends BaseService<Store> {

    /**
     * 商家门店分页查询
     * @param commonPageParam
     * @return
     */
    Paging<StoreVo> getList(CommonPageParam commonPageParam);

    /**
     * 获取所有商家门店
     * @param
     * @return
     */
    List<StoreVo> getAll();

    /**
     * 获取添加商家门店需要的地址信息
     * @param
     * @return
     */
    List<RegionVo> toAdd();

    /**
     * 添加商家门店
     * @param storeParam
     * @return
     */
    Boolean add(StoreParam storeParam);

    /**
     * 获取编辑商家门店需要信息
     * @param storeId
     * @return
     */
    Map<String, Object> toEdit(Integer storeId);

    /**
     * 编辑商家门店
     * @param storeParam
     * @return
     */
    Boolean edit(StoreParam storeParam);

    /**
     * 软删除商家门店
     * @param storeId
     * @return
     */
    Boolean setDelete(Integer storeId);
}
