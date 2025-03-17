package net.jjjshop.front.service.order.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.OrderAdvance;
import net.jjjshop.common.mapper.order.OrderAdvanceMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.front.service.order.OrderAdvanceService;
import org.springframework.stereotype.Service;

/**
 * 预售定金订单记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-04
 */
@Slf4j
@Service
public class OrderAdvanceServiceImpl extends BaseServiceImpl<OrderAdvanceMapper, OrderAdvance> implements OrderAdvanceService {

}
