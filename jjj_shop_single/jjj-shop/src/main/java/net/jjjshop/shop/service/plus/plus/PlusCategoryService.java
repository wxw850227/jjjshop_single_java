package net.jjjshop.shop.service.plus.plus;

import net.jjjshop.common.entity.plus.PlusCategory;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.shop.vo.plus.PlusCategoryVo;

import java.util.List;

/**
 * 插件分类表 服务类
 *
 * @author jjjshop
 * @since 2022-07-23
 */
public interface PlusCategoryService extends BaseService<PlusCategory> {

    /**
     * 插件列表
     * @param
     * @return
     */
    List<PlusCategoryVo> getAll();

}
