package net.jjjshop.front.service.plus.coupon;

import com.alibaba.fastjson.JSONObject;
import net.jjjshop.common.entity.plus.coupon.Coupon;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.front.vo.coupon.CouponDetailVo;
import net.jjjshop.front.vo.coupon.CouponListVo;

import java.util.Date;
import java.util.List;

/**
 * 优惠券记录表 服务类
 * @author jjjshop
 * @since 2022-07-25
 */
public interface CouponService extends BaseService<Coupon> {

    /**
     * 获取待领取优惠券
     * @param user
     * @param limit
     * @return
     */
    List<CouponListVo>  getWaitList(User user, Integer limit);

    /**
     * 优惠券详情
     * @param couponId
     * @param user
     * @return
     */
    CouponDetailVo detail(Integer couponId, User user);

    /**
     * 领取优惠券
     * @param user,couponId
     * @return
     */
    Boolean receive(User user, Integer couponId);

    /**
     * 添加优惠券方法
     * @param user
     * @param couponId
     * @return
     */
    Boolean add(User user, Integer couponId);

    /**
     * 优惠券领取数+1
     * @param coupon
     * @return
     */
    Boolean setIncReceiveNum(Coupon coupon);

    /**
     * 检查优惠券是否可以领取
     * @param couponId
     * @return
     */
    Boolean checkReceive(Integer couponId);

    /**
     * 获取优惠券状态
     * @param coupon
     * @param userId
     * @return
     */
    JSONObject getCouponState(Coupon coupon, Integer userId);

    /**
     * 获取优惠券种类
     * @param value
     * @return
     */
    String getCouponTypeAttr(Integer value);

    /**
     * 获取格式化后的时间
     * @param date
     * @return
     */
    String getTimeAttr(Date date);

}
