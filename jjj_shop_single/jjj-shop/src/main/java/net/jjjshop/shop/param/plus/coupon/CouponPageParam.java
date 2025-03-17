package net.jjjshop.shop.param.plus.coupon;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

/**
 * 优惠券 查询参数对象
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CouponPageParam对象", description = "优惠劵分页查询参数")
public class CouponPageParam extends BasePageOrderParam {
    private final static long serialVersionUID = 1L;
}
