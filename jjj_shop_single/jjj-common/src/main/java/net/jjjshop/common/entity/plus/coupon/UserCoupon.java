package net.jjjshop.common.entity.plus.coupon;

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
 * 用户优惠券记录表
 *
 * @author jjjshop
 * @since 2022-07-26
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_user_coupon")
@ApiModel(value = "UserCoupon对象")
public class UserCoupon implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "user_coupon_id", type = IdType.AUTO)
    private Integer userCouponId;

    @ApiModelProperty("优惠券id")
    private Integer couponId;

    @ApiModelProperty("优惠券名称")
    private String name;

    @ApiModelProperty("优惠券颜色(10蓝 20红 30紫 40黄)")
    private Integer color;

    @ApiModelProperty("优惠券类型(10满减券 20折扣券)")
    private Integer couponType;

    @ApiModelProperty("满减券-减免金额")
    private BigDecimal reducePrice;

    @ApiModelProperty("折扣券-折扣率(0-100)")
    private Integer discount;

    @ApiModelProperty("最低消费金额")
    private BigDecimal minPrice;

    @ApiModelProperty("到期类型(10领取后生效 20固定时间)")
    private Integer expireType;

    @ApiModelProperty("领取后生效-有效天数")
    private Integer expireDay;

    @ApiModelProperty("有效期开始时间")
    private Date startTime;

    @ApiModelProperty("有效期结束时间")
    private Date endTime;

    @ApiModelProperty("适用范围(10全部商品 20指定商品)")
    private Integer applyRange;

    @ApiModelProperty("是否过期(0未过期 1已过期)")
    private Integer isExpire;

    @ApiModelProperty("是否已使用(0未使用 1已使用)")
    private Integer isUse;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("小程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
