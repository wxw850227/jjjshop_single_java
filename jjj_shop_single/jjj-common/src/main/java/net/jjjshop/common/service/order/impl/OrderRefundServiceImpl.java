package net.jjjshop.common.service.order.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.OrderRefund;
import net.jjjshop.common.mapper.order.OrderRefundMapper;
import net.jjjshop.common.service.order.OrderRefundService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderRefundServiceImpl extends BaseServiceImpl<OrderRefundMapper, OrderRefund> implements OrderRefundService {

}
