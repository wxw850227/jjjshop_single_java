package net.jjjshop.shop.service.product;

import net.jjjshop.common.entity.product.ProductSpec;
import net.jjjshop.framework.common.service.BaseService;

import java.util.Map;

/**
 * 商品规格组记录表 服务类
 * @author jjjshop
 * @since 2022-06-28
 */
public interface ProductSpecService extends BaseService<ProductSpec> {
    /**
     * 添加规格
     * @param specName
     * @param specValue
     * @return
     */
    Map<String,Object> addSpec(String specName, String specValue);

    /**
     * 添加规格值
     * @param specId
     * @param specValue
     * @return
     */
    Map<String,Object> addSpecValue(Integer specId, String specValue);
}
