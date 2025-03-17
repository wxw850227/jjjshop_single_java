package net.jjjshop.common.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.system.SystemVersion;
import net.jjjshop.common.mapper.system.SystemVersionMapper;
import net.jjjshop.common.service.system.SystemVersionService;
import net.jjjshop.config.constant.CommonConstant;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统信息表 服务实现类
 *
 * @author jjjshop
 * @since 2022-06-22
 */
@Slf4j
@Service
public class SystemVersionServiceImpl extends BaseServiceImpl<SystemVersionMapper, SystemVersion> implements SystemVersionService {
    @Autowired
    private SystemVersionMapper systemVersionMapper;

    public String getVersion(){
        SystemVersion version = systemVersionMapper.selectOne(new LambdaQueryWrapper<SystemVersion>()
                .comment(CommonConstant.NOT_WITH_App_Id));
        return version.getVersionNo();
    }
}
