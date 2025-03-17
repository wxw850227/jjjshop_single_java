package net.jjjshop.shop.service.settings;

import net.jjjshop.common.entity.settings.Express;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.setting.ExpressPageParam;
import net.jjjshop.shop.param.setting.ExpressParam;

import java.util.List;

/**
 * 物流公司记录表 服务类
 * @author jjjshop
 * @since 2022-07-19
 */
public interface ExpressService extends BaseService<Express> {

    /**
     * 物流公司分页查询
     * @param expressPageParam
     * @return
     */
    Paging<Express> getList(ExpressPageParam expressPageParam);

    /**
     * 获取所有物流公司
     * @param
     * @return
     */
    List<Express> getAll();

    /**
     * 添加物流公司
     * @param expressParam
     * @return
     */
    boolean add(ExpressParam expressParam);

    /**
     * 修改物流公司
     * @param expressParam
     * @return
     */
    boolean edit(ExpressParam expressParam);

    /**
     * 删除物流公司
     * @param id
     * @return
     */
    boolean delById(Integer id);
}
