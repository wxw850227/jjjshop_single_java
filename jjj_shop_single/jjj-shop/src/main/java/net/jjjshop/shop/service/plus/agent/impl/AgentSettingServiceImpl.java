package net.jjjshop.shop.service.plus.agent.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.plus.agent.AgentSetting;
import net.jjjshop.common.mapper.plus.agent.AgentSettingMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.shop.service.plus.agent.AgentSettingService;
import org.springframework.stereotype.Service;

/**
 * 分销商设置表 服务实现类
 * @author jjjshop
 * @since 2022-06-29
 */
@Slf4j
@Service
public class AgentSettingServiceImpl extends BaseServiceImpl<AgentSettingMapper, AgentSetting> implements AgentSettingService {
}
