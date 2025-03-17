package net.jjjshop.common.service.system;

import net.jjjshop.common.entity.system.SystemVersion;
import net.jjjshop.framework.common.service.BaseService;

/**
 * 系统信息表 服务类
 *
 * @author jjjshop
 * @since 2022-06-22
 */
public interface SystemVersionService extends BaseService<SystemVersion> {
    public String getVersion();
}
