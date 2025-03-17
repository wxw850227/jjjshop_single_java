package net.jjjshop.front.service.user.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.plus.coupon.Coupon;
import net.jjjshop.common.entity.plus.coupon.UserCoupon;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.CouponColorEnum;
import net.jjjshop.common.mapper.user.UserCouponMapper;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.front.param.user.UserCouponPageParam;
import net.jjjshop.front.service.plus.coupon.CouponService;
import net.jjjshop.front.service.user.UserCouponService;
import net.jjjshop.front.vo.coupon.UserCouponVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户优惠券记录表 服务实现类
 * @author jjjshop
 * @since 2022-08-02
 */

@Slf4j
@Service
public class UserCouponServiceImpl extends BaseServiceImpl<UserCouponMapper, UserCoupon> implements UserCouponService {

    @Autowired
    private CouponService couponService;

    /**
     * 分页获取用户可用的优惠券
     * @param userId
     * @param userCouponPageParam
     * @return
     */
    public Paging<UserCouponVo> getPageList(Integer userId, UserCouponPageParam userCouponPageParam) {
        LambdaQueryWrapper<UserCoupon> wrapper = new LambdaQueryWrapper<>();
        boolean isUse = false;
        boolean isExpire = false;
        switch (userCouponPageParam.getDataType()) {
            case "not_use":
                isUse = false;
                break;
            case "is_use":
                isUse = true;
                break;
            case "is_expire":
                isExpire = true;
                break;
        }
        wrapper.eq(UserCoupon::getIsUse, isUse ? 1 : 0);
        wrapper.eq(UserCoupon::getIsExpire, isExpire ? 1 : 0);
        wrapper.eq(UserCoupon::getUserId, userId);
        Page page = new PageInfo(userCouponPageParam);
        IPage<UserCoupon> iPage = this.page(page, wrapper);
        IPage<UserCouponVo> result = iPage.convert(item -> {
            UserCouponVo vo = new UserCouponVo();
            BeanUtils.copyProperties(item, vo);
            vo.setColorText(CouponColorEnum.getText(vo.getColor()));
            vo.setCouponTypeText(couponService.getCouponTypeAttr(vo.getCouponType()));
            vo.setState(couponService.getCouponState(couponService.getById(item.getCouponId()), userId));
            vo.setStartTimeText(couponService.getTimeAttr(vo.getStartTime()));
            vo.setEndTimeText(couponService.getTimeAttr(vo.getEndTime()));
            return vo;
        });
        return new Paging(result);
    }

    /**
     * 获取用户可使用优惠券
     * @param userId
     * @param orderTotalPrice
     * @return
     */
    public List<UserCouponVo> getUserCouponList(Integer userId, BigDecimal orderTotalPrice) {
        List<UserCoupon> userCouponList = this.list(new LambdaQueryWrapper<UserCoupon>()
                .eq(UserCoupon::getIsExpire, 0).eq(UserCoupon::getIsUse, 0)
                .eq(UserCoupon::getUserId, userId));
        List<UserCouponVo> voList = new ArrayList<>();
        for (UserCoupon item : userCouponList) {
            // 最低消费金额
            if (orderTotalPrice.compareTo(item.getMinPrice()) < 0) {
                continue;
            }
            // 有效期范围内
            if (item.getStartTime().after(new Date())) {
                continue;
            }
            UserCouponVo vo = new UserCouponVo();
            BeanUtils.copyProperties(item, vo);
            Coupon coupon = couponService.getById(item.getCouponId());
            vo.setApplyRange(coupon.getApplyRange());
            vo.setFreeLimit(coupon.getFreeLimit());
            vo.setCategoryIds(coupon.getCategoryIds());
            vo.setProductIds(coupon.getProductIds());
            // 计算打折金额
            if (item.getCouponType() == 20) {
                BigDecimal reducePrice = orderTotalPrice.multiply(new BigDecimal((coupon.getDiscount() * 0.01))).setScale(2, BigDecimal.ROUND_DOWN);
                vo.setReducedPrice(orderTotalPrice.subtract(reducePrice));
            } else {
                vo.setReducedPrice(coupon.getReducePrice());
            }
            vo.setColorText(CouponColorEnum.getText(vo.getColor()));
            vo.setCouponTypeText(couponService.getCouponTypeAttr(vo.getCouponType()));
            vo.setStartTimeText(couponService.getTimeAttr(vo.getStartTime()));
            vo.setEndTimeText(couponService.getTimeAttr(vo.getEndTime()));
            voList.add(vo);
        }
        // 按抵扣金额降序排序
        return voList.stream().
                sorted(Comparator.comparing(UserCouponVo::getReducedPrice, Comparator.reverseOrder())).
                collect(Collectors.toList());
    }

    /**
     * 获取用户可使用优惠券数量
     * @param userId
     * @return
     */
    public Integer getUserCouponCount(Integer userId) {
        return this.count(new LambdaQueryWrapper<UserCoupon>().eq(UserCoupon::getUserId, userId)
                .eq(UserCoupon::getIsUse, 0).eq(UserCoupon::getIsExpire, 0));
    }

    /**
     * 领取优惠券
     * @param user
     * @param couponId
     * @return
     */
    public Boolean receive(User user, Integer couponId) {
        Coupon coupon = couponService.getById(couponId);
        if (!this.checkReceive(user, coupon)) {
            return false;
        }
        couponService.setIncReceiveNum(coupon);
        return this.add(user, coupon);
    }

    /**
     * 批量获取优惠券
     * @param user
     * @param couponIds
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean receiveList(User user, List<Integer> couponIds) {
        List<Coupon> couponList = couponService.list(new LambdaQueryWrapper<Coupon>().in(Coupon::getCouponId, couponIds));
        List<UserCoupon> userCouponList = new ArrayList<>();
        for (Coupon coupon : couponList) {
            if (this.checkReceive(user, coupon)) {
                Date startTime = null;
                Date endTime = null;
                if (coupon.getExpireType() == 10) {
                    startTime = DateUtil.offsetDay(new Date(), 0);
                    endTime = DateUtil.offsetDay(new Date(), coupon.getExpireDay());
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
                userCoupon.setUserId(user.getUserId());
                userCoupon.setCouponType(coupon.getCouponType());
                coupon.setReceiveNum(coupon.getReceiveNum() + 1);
                couponService.updateById(coupon);
                userCouponList.add(userCoupon);
            } else {
                continue;
            }
        }
        if (CollectionUtils.isNotEmpty(userCouponList)) {
            return this.saveBatch(userCouponList);
        } else {
            return false;
        }
    }

    /**
     * 添加用户优惠券
     * @param user
     * @param coupon
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(User user, Coupon coupon) {
        Date startTime = null;
        Date endTime = null;
        if (coupon.getExpireType() == 10) {
            startTime = DateUtil.offsetDay(new Date(), 0);
            endTime = DateUtil.offsetDay(new Date(), coupon.getExpireDay());
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
        userCoupon.setUserId(user.getUserId());
        userCoupon.setExpireDay(coupon.getExpireDay());
        userCoupon.setCouponType(coupon.getCouponType());
        boolean save = this.save(userCoupon);
        coupon.setReceiveNum(coupon.getReceiveNum() + 1);
        return save;
    }

    /**
     * 获取用户已有的优惠券Id
     * @param user
     * @return
     */
    public List<Integer> getUserCouponIds(User user) {
        return this.list(new LambdaQueryWrapper<UserCoupon>().eq(UserCoupon::getUserId, user.getUserId())).stream().map(e -> {
            return e.getCouponId();
        }).collect(Collectors.toList());
    }

    /**
     * 检测优惠券是否可以使用
     * @param user
     * @param coupon
     * @return
     */
    private Boolean checkReceive(User user, Coupon coupon) {
        if (coupon == null) {
            throw new BusinessException("优惠券不存在");
        }
        if (!couponService.checkReceive(coupon.getCouponId())) {
            return false;
        }
        List<Integer> userCouponIds = this.getUserCouponIds(user);
        if (userCouponIds.contains(coupon.getCouponId())) {
            throw new BusinessException("该优惠券已领取");
        }
        return true;
    }
}
