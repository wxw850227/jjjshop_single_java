package net.jjjshop.front.service.product.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.cache.ProductCategoryCache;
import net.jjjshop.common.entity.product.ProductCategory;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.mapper.product.ProductCategoryMapper;
import net.jjjshop.common.vo.product.CategoryVo;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.front.service.product.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品分类表 服务实现类
 * @author jjjshop
 * @since 2022-06-28
 */
@Slf4j
@Service
public class ProductCategoryServiceImpl extends BaseServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

    @Autowired
    private ProductCategoryCache productCategoryCache;

    /**
     * 商品详情
     * @param productId
     * @param user
     * @return
     */
    public List<CategoryVo> getList(Integer productId, User user) {
        List<CategoryVo> cache = productCategoryCache.getCache();
        return cache;
    }
}
