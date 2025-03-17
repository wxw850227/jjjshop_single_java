package net.jjjshop.front.service.order;

import net.jjjshop.common.entity.order.OrderRefundAddress;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.front.vo.order.OrderRefundAddressVo;

/**
 * 售后单退货地址记录表 服务类
 * @author jjjshop
 * @since 2022-07-04
 */
public interface OrderRefundAddressService extends BaseService<OrderRefundAddress> {

    /**
     * 获取售后单退货地址
     * @param orderRefundId
     * @return
     */
    OrderRefundAddressVo getRefundAddress(Integer orderRefundId);
}
