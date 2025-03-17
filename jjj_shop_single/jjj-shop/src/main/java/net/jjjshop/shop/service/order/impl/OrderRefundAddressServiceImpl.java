package net.jjjshop.shop.service.order.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.OrderRefundAddress;
import net.jjjshop.common.mapper.order.OrderRefundAddressMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.shop.service.order.OrderRefundAddressService;
import org.springframework.stereotype.Service;

/**
 * 售后单退货地址记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-04
 */
@Slf4j
@Service
public class OrderRefundAddressServiceImpl extends BaseServiceImpl<OrderRefundAddressMapper, OrderRefundAddress> implements OrderRefundAddressService {
}
