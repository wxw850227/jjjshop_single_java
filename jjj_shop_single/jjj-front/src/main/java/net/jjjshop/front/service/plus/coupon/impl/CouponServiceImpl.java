package net.jjjshop.front.service.plus.coupon.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.plus.coupon.Coupon;
import net.jjjshop.common.entity.plus.coupon.UserCoupon;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.CouponColorEnum;
import net.jjjshop.common.mapper.plus.coupon.CouponMapper;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.front.service.plus.coupon.CouponService;
import net.jjjshop.front.service.product.ProductService;
import net.jjjshop.front.service.user.UserCouponService;
import net.jjjshop.front.vo.coupon.CouponDetailVo;
import net.jjjshop.front.vo.coupon.CouponListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 优惠券记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-25
 */
@Slf4j
@Service
public class CouponServiceImpl extends BaseServiceImpl<CouponMapper, Coupon> implements CouponService {

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SettingUtils settingUtils;


    /**
     * 获取待领取优惠券
     * @param user
     * @param limit
     * @return
     */
    public List<CouponListVo> getWaitList(User user, Integer limit) {
        LambdaQueryWrapper<Coupon> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Coupon::getIsDelete, 0);
        wrapper.and(i -> i.or().eq(Coupon::getTotalNum, -1)
                .or().apply("(receive_num < total_num)"));
        wrapper.and(i -> i.or().isNull(Coupon::getEndTime)
                .or().apply("(end_time > unix_timestamp())"));
        //排除掉用户已经有的优惠劵
        if (user != null) {
            List<UserCoupon> list = userCouponService.list(new LambdaQueryWrapper<UserCoupon>().eq(UserCoupon::getUserId, user.getUserId()));
            if (list != null && list.size() > 0) {
                List<Integer> couponIds = list.stream().map(e -> {
                    return e.getCouponId();
                }).collect(Collectors.toList());
                wrapper.notIn(Coupon::getCouponId, couponIds);
            }
        }
        wrapper.eq(Coupon::getShowCenter, 1);
        if (limit > 0) {
            wrapper.last("limit " + limit);
        }
        List<CouponListVo> result = this.list(wrapper).stream().map(e -> {
            CouponListVo vo = new CouponListVo();
            BeanUtils.copyProperties(e, vo);
            vo.setColorText(CouponColorEnum.getText(vo.getColor()));
            vo.setCouponTypeText(this.getCouponTypeAttr(vo.getCouponType()));
            vo.setState(this.getCouponState(e, user.getUserId()));
            vo.setStartTimeText(this.getTimeAttr(vo.getStartTime()));
            vo.setEndTimeText(this.getTimeAttr(vo.getEndTime()));
            return vo;
        }).collect(Collectors.toList());

        return result;
    }

    /**
     * 领取优惠券
     * @param user,couponId
     * @return
     */
    public Boolean receive(User user, Integer couponId) {
        if (!this.checkReceive(couponId)) {
            return false;
        }
        return this.add(user, couponId);
    }

    /**
     * 检查优惠券是否可以领取
     * @param couponId
     * @return
     */
    public Boolean checkReceive(Integer couponId) {
        Coupon coupon = this.getById(couponId);
        if (coupon.getTotalNum() != -1 && coupon.getReceiveNum() >= coupon.getTotalNum()) {
            throw new BusinessException("优惠券已发完");
        }
        if (coupon.getExpireType() == 20 && DateUtil.offsetDay(coupon.getEndTime(), 1).isBefore(new Date())) {
            throw new BusinessException("优惠券已过期");
        }
        return true;
    }

    /**
     * 添加优惠券方法
     * @param user
     * @param couponId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(User user, Integer couponId) {
        Coupon coupon = this.getById(couponId);
        if (coupon.getExpireType() == 10) {
            coupon.setStartTime(DateUtil.offsetDay(new Date(), 0));
            coupon.setEndTime(DateUtil.offsetDay(new Date(), +coupon.getExpireDay()));
        }
        UserCoupon userCoupon = new UserCoupon();
        BeanUtils.copyProperties(coupon, userCoupon);
        userCoupon.setUserId(user.getUserId());
        boolean save = userCouponService.save(userCoupon);
        if (save) {
            this.setIncReceiveNum(coupon);
        }
        return save;

    }

    /**
     * 优惠券领取数+1
     * @param coupon
     * @return
     */
    public Boolean setIncReceiveNum(Coupon coupon) {
        return this.update(new LambdaUpdateWrapper<Coupon>().eq(Coupon::getCouponId, coupon.getCouponId())
                .set(Coupon::getReceiveNum, coupon.getReceiveNum() + 1));
    }


    /**
     * 优惠券详情
     * @param couponId
     * @param user
     * @return
     */
    public CouponDetailVo detail(Integer couponId, User user) {
        Coupon coupon = this.getById(couponId);
        CouponDetailVo vo = new CouponDetailVo();
        BeanUtils.copyProperties(coupon, vo);
        vo.setColorText(CouponColorEnum.getText(vo.getColor()));
        vo.setCouponTypeText(this.getCouponTypeAttr(vo.getCouponType()));
        vo.setState(this.getCouponState(coupon, user.getUserId()));
        vo.setStartTimeText(this.getTimeAttr(vo.getStartTime()));
        vo.setEndTimeText(this.getTimeAttr(vo.getEndTime()));
        if (vo.getApplyRange() == 20) {
            String[] split = StringUtils.split(vo.getProductIds(), ",");
            List<Integer> productIds = Arrays.stream(split).map(e -> {
                return Integer.valueOf(e);
            }).collect(Collectors.toList());
            vo.setProductList(productService.getListByProductIds(productIds, user));
        }
        if (vo.getApplyRange() == 30) {
            List<Integer> categoryIds = new ArrayList<>();
            JSONObject json = (JSONObject) JSON.parse(vo.getCategoryIds());
            List<Integer> first = json.getObject("first", List.class);
            List<Integer> second = json.getObject("second", List.class);
            categoryIds.addAll(first);
            categoryIds.addAll(second);
            vo.setProductList(productService.getListByCategoryIds(categoryIds, user));
        }
        return vo;
    }

    /**
     * 获取优惠券状态
     * @param coupon
     * @param userId
     * @return
     */
    public JSONObject getCouponState(Coupon coupon, Integer userId) {
        JSONObject json = new JSONObject();
        int count = userCouponService.count(new LambdaQueryWrapper<UserCoupon>().eq(UserCoupon::getCouponId, coupon.getCouponId())
                .eq(UserCoupon::getUserId, userId));
        if (count > 0) {
            json.put("text", "已领取");
            json.put("value", 0);
            return json;
        }
        if (coupon.getTotalNum() > -1 && coupon.getReceiveNum() > coupon.getTotalNum()) {
            json.put("text", "已抢光");
            json.put("value", 0);
            return json;
        }
        if (coupon.getExpireType() == 20 && coupon.getReceiveNum() > coupon.getTotalNum()) {
            json.put("text", "已过期");
            json.put("value", 0);
            return json;
        }
        json.put("text", "");
        json.put("value", 1);
        return json;
    }

    /**
     * 获取优惠券种类
     * @param value
     * @return
     */
    public String getCouponTypeAttr(Integer value) {
        if (value == 10) {
            return "满减券";
        }
        if (value == 20) {
            return "折扣券";
        }
        return "";
    }

    /**
     * 获取格式化后的时间
     * @param
     * @return
     */
    public String getTimeAttr(Date date) {
        return DateUtil.format(date, "yyyy/MM/dd");
    }
    
}
