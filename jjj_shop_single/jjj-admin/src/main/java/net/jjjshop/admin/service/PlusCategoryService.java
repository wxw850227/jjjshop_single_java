package net.jjjshop.admin.service;

import net.jjjshop.admin.vo.PlusCategoryVo;
import net.jjjshop.common.entity.plus.PlusCategory;
import net.jjjshop.framework.common.service.BaseService;

import java.util.List;

/**
 * 插件分类表 服务类
 *
 * @author jjjshop
 * @since 2022-06-22
 */
public interface PlusCategoryService extends BaseService<PlusCategory> {
    List<PlusCategoryVo> getAll();
}
