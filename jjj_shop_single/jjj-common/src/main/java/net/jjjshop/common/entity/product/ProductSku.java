package net.jjjshop.common.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品规格表
 *
 * @author jjjshop
 * @since 2022-06-28
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_product_sku")
@ApiModel(value = "ProductSku对象")
public class ProductSku implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("产品规格id")
    @TableId(value = "product_sku_id", type = IdType.AUTO)
    private Integer productSkuId;

    @ApiModelProperty("产品id")
    private Integer productId;

    @ApiModelProperty("产品sku记录索引 (由规格id组成)")
    private String specSkuId;

    @ApiModelProperty("规格图片id")
    private Integer imageId;

    @ApiModelProperty("产品编码")
    private String productNo;

    @ApiModelProperty("产品价格")
    private BigDecimal productPrice;

    @ApiModelProperty("产品划线价")
    private BigDecimal linePrice;

    @ApiModelProperty("产品底价")
    private BigDecimal lowPrice;

    @ApiModelProperty("当前库存数量")
    private Integer stockNum;

    @ApiModelProperty("产品销量(废弃)")
    private Integer productSales;

    @ApiModelProperty("产品重量(Kg)")
    private Double productWeight;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
