package net.jjjshop.front.service.store;

import net.jjjshop.common.entity.store.StoreOrder;
import net.jjjshop.framework.common.service.BaseService;

/**
 * 商家门店核销订单记录表 服务类
 * @author jjjshop
 * @since 2022-07-27
 */
public interface StoreOrderService extends BaseService<StoreOrder> {

    /**
     * 添加订单核销记录
     * @param orderId
     * @param storeId
     * @param clerkId
     * @param orderType
     * @return
     */
    Boolean add(Integer orderId, Integer storeId, Integer clerkId,Integer orderType);
}
