package net.jjjshop.shop.service.plus.plus;

import net.jjjshop.common.entity.plus.coupon.UserCoupon;
import net.jjjshop.common.entity.user.UserGrade;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.plus.coupon.SendCouponParam;
import net.jjjshop.shop.param.plus.coupon.UserCouponPageParam;
import net.jjjshop.shop.vo.user.UserCouponVo;

import java.util.List;

/**
 * 用户优惠券记录表 服务类
 * @author jjjshop
 * @since 2022-07-26
 */
public interface UserCouponService extends BaseService<UserCoupon> {


    /**
     * 分页查询用户领券记录
     * @param userCouponPageParam
     * @return
     */
    Paging<UserCouponVo> getList(UserCouponPageParam userCouponPageParam);


    /**
     * 获取发送优惠券需要的数据
     * @param
     * @return
     */
    List<UserGrade> toSendCoupon();

    /**
     * 发送优惠券
     * @param sendCouponParam
     * @return
     */
    Boolean sendCoupon(SendCouponParam sendCouponParam);
}
