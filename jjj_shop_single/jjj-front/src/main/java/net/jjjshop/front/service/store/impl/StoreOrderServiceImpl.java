package net.jjjshop.front.service.store.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.store.StoreOrder;
import net.jjjshop.common.mapper.store.StoreOrderMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.front.service.store.StoreOrderService;
import org.springframework.stereotype.Service;

/**
 * 商家门店核销订单记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-27
 */
@Slf4j
@Service
public class StoreOrderServiceImpl extends BaseServiceImpl<StoreOrderMapper, StoreOrder> implements StoreOrderService {

    /**
     * 添加订单核销记录
     * @param orderId
     * @param storeId
     * @param clerkId
     * @param orderType
     * @return
     */
    public Boolean add(Integer orderId, Integer storeId, Integer clerkId, Integer orderType) {
        StoreOrder storeOrder = new StoreOrder();
        storeOrder.setOrderId(orderId);
        storeOrder.setStoreId(storeId);
        storeOrder.setClerkId(clerkId);
        storeOrder.setOrderType(orderType);
        return this.save(storeOrder);
    }


}
