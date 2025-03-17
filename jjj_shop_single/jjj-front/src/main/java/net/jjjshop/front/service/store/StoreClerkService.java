package net.jjjshop.front.service.store;

import net.jjjshop.common.entity.store.StoreClerk;
import net.jjjshop.framework.common.service.BaseService;

/**
 * 商家门店店员表 服务类
 * @author jjjshop
 * @since 2022-07-27
 */
public interface StoreClerkService extends BaseService<StoreClerk> {

    /**
     * 添加订单核销记录
     * @param storeId
     * @param userId
     * @return
     */
    StoreClerk detail(Integer storeId,Integer userId);
}
