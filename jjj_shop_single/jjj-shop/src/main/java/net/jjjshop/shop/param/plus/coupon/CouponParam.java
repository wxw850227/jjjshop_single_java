package net.jjjshop.shop.param.plus.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 优惠券 查询参数对象
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "CouponParam对象", description = "优惠券参数对象")
public class CouponParam implements Serializable {

    private final static long serialVersionUID = 1L;

    @ApiModelProperty("优惠券id")
    private Integer couponId;

    @NotBlank(message = "优惠券名称不能为空")
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

    @ApiModelProperty("固定时间-开始时间")
    private Date startTime;

    @ApiModelProperty("固定时间-结束时间")
    private Date endTime;

    @ApiModelProperty("适用范围(10全部商品 20指定商品 30指定分类)")
    private Integer applyRange;

    @NotNull(message = "优惠券发放数量不能为空")
    @ApiModelProperty("发放总数量(-1为不限制)")
    private Integer totalNum;

    @ApiModelProperty("已领取数量")
    private Integer receiveNum;

    @ApiModelProperty("排序方式(数字越小越靠前)")
    private Integer sort;

    @ApiModelProperty("是否显示领券中心，0否1是")
    private Integer showCenter;

    @ApiModelProperty("优惠限制0不显示1不可与促销同时2不可与等级优惠同时3不可于促销和等级同时")
    private Integer freeLimit;

    @ApiModelProperty("优惠劵时限数组")
    private Date[] activeTime;

    @ApiModelProperty("限制商品id")
    private List<Integer> productIds;

    @ApiModelProperty("限制一级分类id")
    private List<Integer> categoryListFirst;

    @ApiModelProperty("限制二级分类id")
    private List<Integer> categoryListSecond;

}
