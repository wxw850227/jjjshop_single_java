package net.jjjshop.common.entity.user;

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
 * 用户购物车
 *
 * @author jjjshop
 * @since 2022-08-02
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_user_cart")
@ApiModel(value = "UserCart对象")
public class UserCart implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    @TableId(value = "cart_id", type = IdType.AUTO)
    private Integer cartId;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("商品")
    private Integer productId;

    @ApiModelProperty("商品规格")
    private String specSkuId;

    @ApiModelProperty("商品数量")
    private Integer totalNum;

    @ApiModelProperty("加入时价格")
    private BigDecimal joinPrice;

    @ApiModelProperty("app_id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
