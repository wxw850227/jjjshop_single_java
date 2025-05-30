package net.jjjshop.common.factory.product.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商品减库存vo")
public class UpdateProductStockVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("产品id")
    private Integer productId;

    @ApiModelProperty("库存计算方式(10下单减库存 20付款减库存)")
    private Integer deductStockType;

    @ApiModelProperty("购买数量")
    private Integer totalNum;

    @ApiModelProperty("购买规格")
    private String specSkuId;
}
