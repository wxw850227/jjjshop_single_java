package net.jjjshop.job.service.order.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.mapper.order.OrderMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.job.service.order.OrderService;
import org.springframework.stereotype.Service;

/**
 * 订单记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-04
 */
@Slf4j
@Service
public class OrderServiceImpl extends BaseServiceImpl<OrderMapper, Order> implements OrderService {
}
