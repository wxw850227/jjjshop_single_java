package net.jjjshop.job.service.user;

import net.jjjshop.common.entity.plus.coupon.UserCoupon;
import net.jjjshop.framework.common.service.BaseService;

import java.util.List;

/**
 * 用户优惠券记录表 服务类
 * @author jjjshop
 * @since 2022-08-02
 */
public interface UserCouponService extends BaseService<UserCoupon> {

    /**
     * 获取已过期的优惠券ID集
     */
    List<Integer> getExpiredCouponIds();

    /**
     * 设置优惠券过期状态
     * @param couponIds
     * @return
     */
    Boolean setIsExpire(List<Integer> couponIds);
}
