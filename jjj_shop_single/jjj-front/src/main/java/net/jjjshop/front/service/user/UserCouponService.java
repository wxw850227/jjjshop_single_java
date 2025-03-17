package net.jjjshop.front.service.user;

import net.jjjshop.common.entity.plus.coupon.Coupon;
import net.jjjshop.common.entity.plus.coupon.UserCoupon;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.front.param.user.UserCouponPageParam;
import net.jjjshop.front.vo.coupon.UserCouponVo;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户优惠券记录表 服务类
 *
 * @author jjjshop
 * @since 2022-08-02
 */
public interface UserCouponService extends BaseService<UserCoupon> {


    /**
     * 分页获取用户可用的优惠券
     * @param userId
     * @param userCouponPageParam
     * @return
     */
    Paging<UserCouponVo> getPageList(Integer userId, UserCouponPageParam userCouponPageParam);

    /**
     * 获取用户可使用优惠券
     * @param userId
     * @param orderTotalPrice
     * @return
     */
    List<UserCouponVo>  getUserCouponList(Integer userId, BigDecimal orderTotalPrice);

    /**
     * 获取用户可使用优惠券数量
     * @param userId
     * @return
     */
    Integer getUserCouponCount(Integer userId);

    /**
     * 领取优惠券
     * @param user
     * @param couponId
     * @return
     */
    Boolean receive(User user, Integer couponId);

    /**
     * 批量获取优惠券
     * @param user
     * @param couponIds
     * @return
     */
    Boolean receiveList(User user, List<Integer> couponIds);

    /**
     * 添加用户优惠券
     * @param user
     * @param coupon
     * @return
     */
    Boolean add(User user, Coupon coupon);

    /**
     * 获取用户已有的优惠券Id
     * @param user
     * @return
     */
    List<Integer> getUserCouponIds(User user);
}
