package net.jjjshop.front.service.store;

import net.jjjshop.common.entity.store.Store;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.front.vo.store.StoreVo;

import java.util.List;

/**
 * 商家门店记录表 服务类
 * @author jjjshop
 * @since 2022-07-27
 */
public interface StoreService extends BaseService<Store> {

    /**
     * 通过id集合查找门店
     * @param storeIds
     * @return
     */
    List<StoreVo> getListByStoreIds(List<Integer> storeIds);

    /**
     * 通过id查找门店
     * @param storeId
     * @return
     */
    StoreVo getStoreVoById(Integer storeId);

    /**
     * 查找门店
     * @param limit
     * @return
     */
    List<StoreVo> getList(Integer limit);

    /**
     * 查询所有可核销门店
     * @param longitude
     * @param latitude
     * @return
     */
    List<StoreVo> getAllCheck(String longitude, String latitude);
}
