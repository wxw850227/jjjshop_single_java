package net.jjjshop.common.service.app.impl;

import net.jjjshop.common.entity.app.AppUpdate;
import net.jjjshop.common.mapper.app.AppUpdateMapper;
import net.jjjshop.common.service.app.AppUpdateService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * app升级记录表 服务实现类
 *
 * @author jjjshop
 * @since 2022-07-27
 */
@Slf4j
@Service
public class AppUpdateServiceImpl extends BaseServiceImpl<AppUpdateMapper, AppUpdate> implements AppUpdateService {

    @Autowired
    private AppUpdateMapper appUpdateMapper;

}
