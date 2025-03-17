package net.jjjshop.shop.param.plus.coupon;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 用户优惠券 查询参数对象
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "UserCouponParam对象", description = "用户优惠券参数")
public class UserCouponParam implements Serializable {
    private final static long serialVersionUID = 1L;
}
