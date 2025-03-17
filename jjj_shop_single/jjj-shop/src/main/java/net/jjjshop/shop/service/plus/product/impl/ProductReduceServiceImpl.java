package net.jjjshop.shop.service.plus.product.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.plus.product.ProductReduce;
import net.jjjshop.common.mapper.plus.product.ProductReduceMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.shop.service.plus.product.ProductReduceService;
import org.springframework.stereotype.Service;

/**
 * 商品满减表 服务实现类
 * @author jjjshop
 * @since 2022-06-28
 */
@Slf4j
@Service
public class ProductReduceServiceImpl extends BaseServiceImpl<ProductReduceMapper, ProductReduce> implements ProductReduceService {
}
