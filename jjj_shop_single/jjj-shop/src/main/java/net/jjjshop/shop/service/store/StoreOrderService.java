package net.jjjshop.shop.service.store;

import net.jjjshop.common.entity.store.StoreOrder;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.store.StoreOrderPageParam;
import net.jjjshop.shop.vo.store.StoreOrderVo;

/**
 * 商家门店核销订单记录表 服务类
 * @author jjjshop
 * @since 2022-07-27
 */
public interface StoreOrderService extends BaseService<StoreOrder> {


    /**
     * 核销记录分页列表
     * @param storeOrderPageParam
     * @return
     */
    Paging<StoreOrderVo> getList(StoreOrderPageParam storeOrderPageParam);

    /**
     * 添加订单核销记录
     * @param
     * @return
     */
    Boolean add(Integer orderId, Integer storeId, Integer clerkId,Integer orderType);
}
