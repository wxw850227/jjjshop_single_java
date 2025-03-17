package net.jjjshop.common.vo.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.shop.ProductSpecRel;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商品规格VO")
public class ProductSpecRelVo extends ProductSpecRel {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("规格名称")
    private String specName;

    @ApiModelProperty("规格值")
    private String specValue;
}
