package net.jjjshop.shop.service.order.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.OrderAddress;
import net.jjjshop.common.mapper.order.OrderAddressMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.shop.service.order.OrderAddressService;
import org.springframework.stereotype.Service;

/**
 * 订单收货地址记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-04
 */
@Slf4j
@Service
public class OrderAddressServiceImpl  extends BaseServiceImpl<OrderAddressMapper, OrderAddress> implements OrderAddressService {
}
