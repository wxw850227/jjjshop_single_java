package net.jjjshop.front.service.settings;

import net.jjjshop.common.entity.settings.Express;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.front.vo.settings.ExpressListVo;

import java.util.List;

/**
 * 物流公司记录表 服务类
 * @author jjjshop
 * @since 2022-07-19
 */
public interface ExpressService extends BaseService<Express> {

    /**
     * 获取所有物流公司
     * @param
     * @return
     */
    List<ExpressListVo> getAll();

}
