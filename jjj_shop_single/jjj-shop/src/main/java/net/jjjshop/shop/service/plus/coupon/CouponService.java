package net.jjjshop.shop.service.plus.coupon;

import net.jjjshop.common.entity.plus.coupon.Coupon;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.plus.coupon.CouponPageParam;
import net.jjjshop.shop.param.plus.coupon.CouponParam;
import net.jjjshop.shop.vo.plus.coupon.CouponVo;

/**
 * 优惠券记录表 服务类
 *
 * @author jjjshop
 * @since 2022-07-25
 */
public interface CouponService extends BaseService<Coupon> {

    /**
     * 优惠券列表
     * @param couponPageParam
     * @return
     */
    Paging<CouponVo> getList(CouponPageParam couponPageParam);

    /**
     * 添加优惠券
     * @param couponParam
     * @return
     */
    Boolean add(CouponParam couponParam);

    /**
     * 编辑优惠券
     * @param couponParam
     * @return
     */
    Boolean edit(CouponParam couponParam);

    /**
     * 软删除优惠券
     * @param couponId
     * @return
     */
    Boolean setDelete(Integer couponId);

    /**
     * 获取单个优惠券详情
     * @param couponId
     * @return
     */
    CouponVo couponDetail(Integer couponId);
}
