package net.jjjshop.common.service.product.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.ProductSpecValue;
import net.jjjshop.common.mapper.product.ProductSpecValueMapper;
import net.jjjshop.common.service.product.ProductSpecValueService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 商品规格值记录表 服务实现类
 *
 * @author jjjshop
 * @since 2022-06-28
 */
@Slf4j
@Service
public class ProductSpecValueServiceImpl extends BaseServiceImpl<ProductSpecValueMapper, ProductSpecValue> implements ProductSpecValueService {

    @Autowired
    private ProductSpecValueMapper productSpecValueMapper;

}
