package net.jjjshop.common.service.product.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.shop.ProductSpecRel;
import net.jjjshop.common.mapper.product.ProductSpecRelMapper;
import net.jjjshop.common.service.product.ProductSpecRelService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 商品与规格值关系记录表 服务实现类
 *
 * @author jjjshop
 * @since 2022-06-28
 */
@Slf4j
@Service
public class ProductSpecRelServiceImpl extends BaseServiceImpl<ProductSpecRelMapper, ProductSpecRel> implements ProductSpecRelService {

}
