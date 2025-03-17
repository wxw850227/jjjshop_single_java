package net.jjjshop.common.entity.plus.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 商品满减表
 *
 * @author jjjshop
 * @since 2022-06-28
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_product_reduce")
@ApiModel(value = "ProductReduce对象")
public class ProductReduce implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "产品id不能为空")
    @ApiModelProperty("产品id")
    @TableId(value = "product_id", type = IdType.INPUT)
    private Integer productId;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Integer createTime;

    @ApiModelProperty("更新时间")
    private Integer updateTime;

}
