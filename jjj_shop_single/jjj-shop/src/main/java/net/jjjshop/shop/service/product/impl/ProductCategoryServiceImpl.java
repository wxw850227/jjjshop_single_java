package net.jjjshop.shop.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.cache.ProductCategoryCache;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.entity.product.ProductCategory;
import net.jjjshop.common.mapper.product.ProductCategoryMapper;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.shop.param.product.CategoryParam;
import net.jjjshop.shop.service.product.ProductCategoryService;
import net.jjjshop.shop.service.product.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 产品分类表 服务实现类
 * @author jjjshop
 * @since 2022-06-28
 */
@Slf4j
@Service
public class ProductCategoryServiceImpl extends BaseServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryCache productCategoryCache;

    /**
     * 新增
     * @param categoryParam
     * @return
     */
    public Boolean add(CategoryParam categoryParam){
        ProductCategory category = new ProductCategory();
        BeanUtils.copyProperties(categoryParam, category);
        // 删除缓存
        productCategoryCache.clearCache();
        return this.save(category);
    }
    /**
     * 新增
     * @param categoryParam
     * @return
     */
    public Boolean edit(CategoryParam categoryParam){
        ProductCategory category = new ProductCategory();
        BeanUtils.copyProperties(categoryParam, category);
        // 删除缓存
        productCategoryCache.clearCache();
        return this.updateById(category);
    }

    /**
     * 真删除
     * @param id
     * @return
     */
    public Boolean delById(Integer id){
        // 是否存在子菜单
        if(this.count(new LambdaQueryWrapper<ProductCategory>().eq(ProductCategory::getParentId, id)) > 0){
            throw new BusinessException("当前菜单下存在子权限，请先删除");
        }
        int productCount = productService.count(new LambdaQueryWrapper<Product>().
                eq(Product::getCategoryId, id).eq(Product::getIsDelete, 0));
        if(productCount > 0){
            throw new BusinessException("该分类下存在"+productCount+"个商品，不允许删除");
        }
        // 删除缓存
        productCategoryCache.clearCache();
        return this.removeById(id);
    }
}
