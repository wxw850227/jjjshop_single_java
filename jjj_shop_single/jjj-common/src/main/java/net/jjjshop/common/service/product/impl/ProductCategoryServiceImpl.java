package net.jjjshop.common.service.product.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.ProductCategory;
import net.jjjshop.common.mapper.product.ProductCategoryMapper;
import net.jjjshop.common.service.product.ProductCategoryService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 产品分类表 服务实现类
 *
 * @author jjjshop
 * @since 2022-06-28
 */
@Slf4j
@Service
public class ProductCategoryServiceImpl extends BaseServiceImpl<ProductCategoryMapper, ProductCategory> implements ProductCategoryService {

}
