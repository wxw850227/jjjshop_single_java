package net.jjjshop.front.vo.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel("首页推送优惠券VO")
public class HomeCouponVo  implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("优惠券id")
    private Integer couponId;

    @ApiModelProperty("优惠券名称")
    private String name;

    @ApiModelProperty("优惠券类型(10满减券 20折扣券)")
    private Integer couponType;

    @ApiModelProperty("满减券-减免金额")
    private BigDecimal reducePrice;

    @ApiModelProperty("折扣券-折扣率(0-100)")
    private Integer discount;

    @ApiModelProperty("最低消费金额")
    private BigDecimal minPrice;
}
