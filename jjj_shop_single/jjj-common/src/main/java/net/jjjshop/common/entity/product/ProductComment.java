package net.jjjshop.common.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.validator.groups.Update;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 订单评价记录表
 *
 * @author jjjshop
 * @since 2022-06-28
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_product_comment")
@ApiModel(value = "ProductComment对象")
public class ProductComment implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("评价id")
    @TableId(value = "comment_id", type = IdType.AUTO)
    @NotNull(message = "id不能为空", groups = {Update.class})
    private Integer commentId;

    @ApiModelProperty("评分 (10好评 20中评 30差评)")
    private Integer score;

    @ApiModelProperty("评价内容")
    private String content;

    @ApiModelProperty("是否为图片评价")
    private Integer isPicture;

    @ApiModelProperty("评价排序")
    private Integer sort;

    @ApiModelProperty("评价状态(0=待审核 1=审核通过2=审核不通过)")
    private Integer status;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("商品id")
    private Integer productId;

    @ApiModelProperty("订单商品id")
    private Integer orderProductId;

    @ApiModelProperty("小程序id")
    private Integer appId;

    @ApiModelProperty("软删除")
    private Integer isDelete;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
