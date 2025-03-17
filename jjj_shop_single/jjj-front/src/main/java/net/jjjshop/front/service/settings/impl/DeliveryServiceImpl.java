package net.jjjshop.front.service.settings.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.settings.Delivery;
import net.jjjshop.common.mapper.settings.DeliveryMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.front.service.settings.DeliveryService;
import org.springframework.stereotype.Service;

/**
 * 配送模板主表 服务实现类
 * @author jjjshop
 * @since 2022-06-29
 */
@Slf4j
@Service
public class DeliveryServiceImpl extends BaseServiceImpl<DeliveryMapper, Delivery> implements DeliveryService {
}
