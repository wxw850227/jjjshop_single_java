package net.jjjshop.common.service.user.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.UserGradeLog;
import net.jjjshop.common.mapper.user.UserGradeLogMapper;
import net.jjjshop.common.service.user.UserGradeLogService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 用户会员等级变更记录表 服务实现类
 *
 * @author jjjshop
 * @since 2022-07-21
 */
@Slf4j
@Service
public class UserGradeLogServiceImpl extends BaseServiceImpl<UserGradeLogMapper, UserGradeLog> implements UserGradeLogService {

}
