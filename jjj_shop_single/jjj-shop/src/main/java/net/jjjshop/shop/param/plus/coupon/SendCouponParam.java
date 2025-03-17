package net.jjjshop.shop.param.plus.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "SendCouponParam对象", description = "发送优惠券参数")
public class SendCouponParam implements Serializable {
    private final static long serialVersionUID = 1L;

    @ApiModelProperty("优惠券名字")
    private String name;

    @ApiModelProperty("发送类型")
    private Integer sendType;

    @ApiModelProperty("用户Id")
    private List<Integer> userIds;

    @ApiModelProperty("用户等级")
    private Integer userLevel;

    @ApiModelProperty("优惠券Id")
    private Integer couponId;
}
