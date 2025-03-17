package net.jjjshop.common.util;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.plus.coupon.UserCoupon;
import net.jjjshop.common.service.user.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserCouponUtils {
    @Autowired
    private UserCouponService userCouponService;

    /**
     * 设置优惠券使用状态
     * @param userCouponId
     * @param isUse
     * @return
     */
    public boolean setIsUse(Integer userCouponId, Integer isUse){
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setUserCouponId(userCouponId);
        userCoupon.setIsUse(isUse);
        return userCouponService.updateById(userCoupon);
    }
}
