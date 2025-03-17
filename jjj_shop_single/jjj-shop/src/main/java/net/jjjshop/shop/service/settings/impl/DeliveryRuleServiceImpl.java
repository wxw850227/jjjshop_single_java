package net.jjjshop.shop.service.settings.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.settings.DeliveryRule;
import net.jjjshop.common.mapper.settings.DeliveryRuleMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.shop.service.settings.DeliveryRuleService;
import org.springframework.stereotype.Service;

/**
 * 配送模板区域及运费表 服务实现类
 * @author jjjshop
 * @since 2022-06-29
 */
@Slf4j
@Service
public class DeliveryRuleServiceImpl extends BaseServiceImpl<DeliveryRuleMapper, DeliveryRule> implements DeliveryRuleService {
}
