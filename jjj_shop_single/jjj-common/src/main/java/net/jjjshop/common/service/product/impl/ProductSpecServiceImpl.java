package net.jjjshop.common.service.product.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.ProductSpec;
import net.jjjshop.common.mapper.product.ProductSpecMapper;
import net.jjjshop.common.service.product.ProductSpecService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 商品规格组记录表 服务实现类
 *
 * @author jjjshop
 * @since 2022-06-28
 */
@Slf4j
@Service
public class ProductSpecServiceImpl extends BaseServiceImpl<ProductSpecMapper, ProductSpec> implements ProductSpecService {

}
