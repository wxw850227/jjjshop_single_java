package net.jjjshop.shop.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.ProductSpec;
import net.jjjshop.common.entity.product.ProductSpecValue;
import net.jjjshop.common.mapper.product.ProductSpecMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.shop.service.product.ProductSpecService;
import net.jjjshop.shop.service.product.ProductSpecValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 商品规格组记录表 服务实现类
 * @author jjjshop
 * @since 2022-06-28
 */
@Slf4j
@Service
public class ProductSpecServiceImpl extends BaseServiceImpl<ProductSpecMapper, ProductSpec> implements ProductSpecService {
    @Autowired
    private ProductSpecValueService productSpecValueService;
    /**
     * 添加规格
     * @param specName
     * @param specValue
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String,Object> addSpec(String specName, String specValue){
        Map<String,Object> result = new HashMap<>();
        // 判断规格组是否存在
        ProductSpec spec = this.getOne(new LambdaQueryWrapper<ProductSpec>().eq(ProductSpec::getSpecName, specName));
        if (spec == null) {
            // 新增规格组
            ProductSpec addSpec = new ProductSpec();
            addSpec.setSpecName(specName);
            this.save(addSpec);
            // 新增规格值
            ProductSpecValue addSpecValue = new ProductSpecValue();
            addSpecValue.setSpecId(addSpec.getSpecId());
            addSpecValue.setSpecValue(specValue);
            productSpecValueService.save(addSpecValue);

            result.put("specId", addSpec.getSpecId());
            result.put("specValueId", addSpecValue.getSpecValueId());
            return result;
        }
        // 判断规格值是否存在
        ProductSpecValue value = productSpecValueService.getOne(new LambdaQueryWrapper<ProductSpecValue>()
                .eq(ProductSpecValue::getSpecId, spec.getSpecId()).eq(ProductSpecValue::getSpecValue, specValue));
        if (value != null) {
            result.put("specId", spec.getSpecId());
            result.put("specValueId", value.getSpecValueId());
            return result;
        }else{
            // 添加规格值
            ProductSpecValue addSpecValue = new ProductSpecValue();
            addSpecValue.setSpecId(spec.getSpecId());
            addSpecValue.setSpecValue(specValue);
            productSpecValueService.save(addSpecValue);

            result.put("specId", spec.getSpecId());
            result.put("specValueId", addSpecValue.getSpecValueId());
            return result;
        }
    }

    /**
     * 添加规格值
     * @param specId
     * @param specValue
     * @return
     */
    public Map<String,Object> addSpecValue(Integer specId, String specValue){
        Map<String,Object> result = new HashMap<>();
        // 判断规格值是否存在
        ProductSpecValue value = productSpecValueService.getOne(new LambdaQueryWrapper<ProductSpecValue>()
                .eq(ProductSpecValue::getSpecId, specId).eq(ProductSpecValue::getSpecValue, specValue));
        if (value != null) {
            result.put("specValueId", value.getSpecValueId());
            return result;
        }else{
            // 添加规格值
            ProductSpecValue addSpecValue = new ProductSpecValue();
            addSpecValue.setSpecId(specId);
            addSpecValue.setSpecValue(specValue);
            productSpecValueService.save(addSpecValue);

            result.put("specValueId", addSpecValue.getSpecValueId());
            return result;
        }
    }
}
