package net.jjjshop.front.service.order.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.OrderRefundImage;
import net.jjjshop.common.mapper.order.OrderRefundImageMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.front.service.order.OrderRefundImageService;
import org.springframework.stereotype.Service;

/**
 * 售后单图片记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-04
 */
@Slf4j
@Service
public class OrderRefundImageServiceImpl extends BaseServiceImpl<OrderRefundImageMapper, OrderRefundImage> implements OrderRefundImageService {
}
