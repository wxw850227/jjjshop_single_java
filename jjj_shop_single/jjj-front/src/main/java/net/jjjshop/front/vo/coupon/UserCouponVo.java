package net.jjjshop.front.vo.coupon;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
@ApiModel("用户优惠券VO")
public class UserCouponVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Integer userCouponId;

    @ApiModelProperty("优惠券id")
    private Integer couponId;

    @ApiModelProperty("优惠券名称")
    private String name;

    @ApiModelProperty("优惠券颜色(10蓝 20红 30紫 40黄)")
    private Integer color;

    @ApiModelProperty("优惠券颜色文本")
    private String colorText;

    @ApiModelProperty("优惠券类型(10满减券 20折扣券)")
    private Integer couponType;

    @ApiModelProperty("优惠券类型(10满减券 20折扣券)")
    private String couponTypeText;

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

    @ApiModelProperty("有效期开始时间")
    private String startTimeText;

    @ApiModelProperty("有效期结束时间")
    private Date endTime;

    @ApiModelProperty("有效期开始时间")
    private String endTimeText;

    @ApiModelProperty("是否过期(0未过期 1已过期)")
    private Integer isExpire;

    @ApiModelProperty("是否已使用(0未使用 1已使用)")
    private Integer isUse;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("优惠券状态")
    private JSONObject state;

    // 优惠券主表字段
    @ApiModelProperty("适用范围(10全部商品 20指定商品)")
    private Integer applyRange;

    @ApiModelProperty("优惠限制0不显示1不可与促销同时2不可与等级优惠同时3不可于促销和等级同时")
    private Integer freeLimit;

    @ApiModelProperty("限制商品id")
    private String productIds;

    @ApiModelProperty("限制分类id")
    private String categoryIds;

    @ApiModelProperty("打折金额")
    private BigDecimal reducedPrice;
}
