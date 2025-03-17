package net.jjjshop.common.mapper.product;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.common.entity.shop.ProductSpecRel;

import org.springframework.stereotype.Repository;


/**
 * 商品与规格值关系记录表 Mapper 接口
 *
 * @author jjjshop
 * @since 2022-06-28
 */
@Repository
public interface ProductSpecRelMapper extends BaseMapper<ProductSpecRel> {


}
