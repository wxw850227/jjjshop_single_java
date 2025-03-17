package net.jjjshop.common.service.app.impl;

import net.jjjshop.common.entity.app.AppOpen;
import net.jjjshop.common.mapper.app.AppOpenMapper;
import net.jjjshop.common.service.app.AppOpenService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * app应用记录表 服务实现类
 *
 * @author jjjshop
 * @since 2022-07-27
 */
@Slf4j
@Service
public class AppOpenServiceImpl extends BaseServiceImpl<AppOpenMapper, AppOpen> implements AppOpenService {

    @Autowired
    private AppOpenMapper appOpenMapper;

}
