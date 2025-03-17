package net.jjjshop.shop.service.plus.plus.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.plus.coupon.Coupon;
import net.jjjshop.common.entity.plus.coupon.UserCoupon;
import net.jjjshop.common.entity.user.UserGrade;
import net.jjjshop.common.mapper.user.UserCouponMapper;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.plus.coupon.SendCouponParam;
import net.jjjshop.shop.param.plus.coupon.UserCouponPageParam;
import net.jjjshop.shop.service.plus.coupon.CouponService;
import net.jjjshop.shop.service.plus.plus.UserCouponService;
import net.jjjshop.shop.service.user.UserGradeService;
import net.jjjshop.shop.service.user.UserService;
import net.jjjshop.shop.vo.user.UserCouponVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户优惠券记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-26
 */
@Slf4j
@Service
public class UserCouponServiceImpl extends BaseServiceImpl<UserCouponMapper, UserCoupon> implements UserCouponService {

    @Autowired
    private UserService userService;
    @Autowired
    private UserGradeService userGradeService;
    @Autowired
    private CouponService couponService;

    /**
     * 分页查询用户领券记录
     * @param userCouponPageParam
     * @return
     */
    public Paging<UserCouponVo> getList(UserCouponPageParam userCouponPageParam) {
        Page page = new PageInfo(userCouponPageParam);
        IPage<UserCoupon> iPage = this.page(page, new LambdaQueryWrapper<UserCoupon>().orderByDesc(UserCoupon::getCreateTime));
        IPage<UserCouponVo> result = iPage.convert(item -> {
            UserCouponVo vo = new UserCouponVo();
            BeanUtils.copyProperties(item, vo);
            vo.setNickname(userService.getById(vo.getUserId()).getNickname());
            vo.setStartTimeText(DateUtil.format(vo.getStartTime(), "yyyy/MM/dd"));
            vo.setEndTimeText(DateUtil.format(vo.getStartTime(), "yyyy/MM/dd"));
            return vo;
        });
        return new Paging(result);
    }

    /**
     * 获取发送优惠券需要的数据
     * @param
     * @return
     */
    public List<UserGrade> toSendCoupon() {
        return userGradeService.getAll();
    }

    /**
     * 发送优惠券
     * @param sendCouponParam
     * @return
     */
    public Boolean sendCoupon(SendCouponParam sendCouponParam) {
        Coupon coupon = couponService.getById(sendCouponParam.getCouponId());
        if (coupon == null) {
            throw new BusinessException("未找到优惠券信息");
        }
        List<UserCoupon> userCouponList = null;
        if (sendCouponParam.getSendType() == 1) {
            List<Integer> userIds = userService.getUserIds();
            if (userIds.size() == 0) {
                throw new BusinessException("没有符合条件的会员");
            }
            userCouponList = this.setData(coupon, userIds);
        } else if (sendCouponParam.getSendType() == 2) {
            List<Integer> userIds = userService.getUserIdsByGrade(sendCouponParam.getUserLevel());
            if (userIds.size() == 0) {
                throw new BusinessException("没有符合条件的会员");
            }
            userCouponList = this.setData(coupon, userIds);
        } else if (sendCouponParam.getSendType() == 3) {
            if (CollectionUtils.isEmpty(sendCouponParam.getUserIds())) {
                throw new BusinessException("请选择用户");
            }
            userCouponList = this.setData(coupon, sendCouponParam.getUserIds());
        }
        return this.saveBatch(userCouponList);
    }

    /**
     * 获取需要保存的UserCoupon数据
     * @param coupon
     * @param userIds
     * @return
     */
    public List<UserCoupon> setData(Coupon coupon, List<Integer> userIds) {
        List<UserCoupon> userCouponList = new ArrayList<>();
        for (Integer userId : userIds) {
            Date startTime = null;
            Date endTime = null;
            if (coupon.getExpireType() == 10) {
                startTime = DateUtil.offsetDay(new Date(), 0);
                endTime = DateUtil.offsetDay(new Date(), +coupon.getExpireDay());
            } else {
                startTime = coupon.getStartTime();
                endTime = coupon.getEndTime();
            }
            UserCoupon userCoupon = new UserCoupon();
            userCoupon.setCouponId(coupon.getCouponId());
            userCoupon.setName(coupon.getName());
            userCoupon.setColor(coupon.getColor());
            userCoupon.setReducePrice(coupon.getReducePrice());
            userCoupon.setDiscount(coupon.getDiscount());
            userCoupon.setMinPrice(coupon.getMinPrice());
            userCoupon.setExpireType(coupon.getExpireType());
            userCoupon.setStartTime(startTime);
            userCoupon.setEndTime(endTime);
            userCoupon.setApplyRange(coupon.getApplyRange());
            userCoupon.setUserId(userId);
            userCoupon.setCouponType(coupon.getCouponType());
            userCoupon.setExpireDay(coupon.getExpireDay());
            userCouponList.add(userCoupon);
        }
        return userCouponList;
    }

}
