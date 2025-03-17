package net.jjjshop.shop.service.store;

import net.jjjshop.common.entity.store.StoreClerk;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.store.StoreClerkPageParam;
import net.jjjshop.shop.param.store.StoreClerkParam;
import net.jjjshop.shop.vo.store.StoreClerkVo;

import java.util.List;
import java.util.Map;

/**
 * 商家门店店员表 服务类
 * @author jjjshop
 * @since 2022-07-27
 */
public interface StoreClerkService extends BaseService<StoreClerk> {

    /**
     * 商家门店店员分页列表
     * @param storeClerkPageParam
     * @return
     */
    Paging<StoreClerkVo> getList(StoreClerkPageParam storeClerkPageParam);

    /**
     * 获取所有商家门店店员列表
     * @param
     * @return
     */
    List<StoreClerkVo> getAll();

    /**
     * 通过商家门店Id获取店员列表
     * @param storeId
     * @return
     */
    List<StoreClerkVo> getClerkByStoreId(Integer storeId);

    /**
     * 获取添加商家店员需要的数据
     * @param
     * @return
     */
    Map<String, Object> toAdd();

    /**
     * 添加商家店员
     * @param storeClerkParam
     * @return
     */
    Boolean add(StoreClerkParam storeClerkParam);

    /**
     * 获取编辑商家店员需要的数据
     * @param clerkId
     * @return
     */
    StoreClerkVo toEdit(Integer clerkId);

    /**
     * 编辑商家店员
     * @param storeClerkParam
     * @return
     */
    Boolean edit(StoreClerkParam storeClerkParam);

    /**
     * 软删除商家店员
     * @param clerkId
     * @return
     */
    Boolean setDelete(Integer clerkId);

}
