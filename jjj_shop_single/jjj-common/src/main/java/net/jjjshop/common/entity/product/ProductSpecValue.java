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
import java.util.Date;

/**
 * 商品规格值记录表
 *
 * @author jjjshop
 * @since 2022-06-28
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_product_spec_value")
@ApiModel(value = "ProductSpecValue对象")
public class ProductSpecValue implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("规格值id")
    @TableId(value = "spec_value_id", type = IdType.AUTO)
    private Integer specValueId;

    @ApiModelProperty("规格值")
    private String specValue;

    @ApiModelProperty("规格组id")
    private Integer specId;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

}
