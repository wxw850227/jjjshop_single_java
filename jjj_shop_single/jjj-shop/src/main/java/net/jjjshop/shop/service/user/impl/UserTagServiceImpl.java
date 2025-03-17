package net.jjjshop.shop.service.user.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.UserTag;
import net.jjjshop.common.mapper.user.UserTagMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.shop.service.user.UserTagService;
import org.springframework.stereotype.Service;

/**
 * 用户标签表 服务实现类
 * @author jjjshop
 * @since 2022-07-21
 */
@Slf4j
@Service
public class UserTagServiceImpl extends BaseServiceImpl<UserTagMapper, UserTag> implements UserTagService {
}
