package net.jjjshop.shop.service.product;

import net.jjjshop.common.entity.product.ProductCategory;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.shop.param.product.CategoryParam;

/**
 * 产品分类表 服务类
 * @author jjjshop
 * @since 2022-06-28
 */
public interface ProductCategoryService extends BaseService<ProductCategory> {
    /**
     * 新增
     * @param categoryParam
     * @return
     */
    Boolean add(CategoryParam categoryParam);
    /**
     * 修改
     * @param categoryParam
     * @return
     */
    Boolean edit(CategoryParam categoryParam);
    /**
     * 真删除
     * @param id
     * @return
     */
    Boolean delById(Integer id);
}
