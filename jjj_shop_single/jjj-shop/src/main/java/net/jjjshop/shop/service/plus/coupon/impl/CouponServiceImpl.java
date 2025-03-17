package net.jjjshop.shop.service.plus.coupon.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.plus.coupon.Coupon;
import net.jjjshop.common.entity.product.ProductCategory;
import net.jjjshop.common.mapper.plus.coupon.CouponMapper;
import net.jjjshop.common.vo.product.CategoryVo;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.plus.coupon.CouponPageParam;
import net.jjjshop.shop.param.plus.coupon.CouponParam;
import net.jjjshop.shop.service.plus.coupon.CouponService;
import net.jjjshop.shop.service.product.ProductCategoryService;
import net.jjjshop.shop.service.product.ProductService;
import net.jjjshop.shop.vo.plus.coupon.CouponVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 优惠券记录表 服务实现类
 *
 * @author jjjshop
 * @since 2022-07-25
 */
@Slf4j
@Service
public class CouponServiceImpl extends BaseServiceImpl<CouponMapper, Coupon> implements CouponService {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * 优惠券列表
     * @param couponPageParam
     * @return
     */
    public Paging<CouponVo> getList(CouponPageParam couponPageParam) {
        Page<Coupon> page = new PageInfo<>(couponPageParam);
        IPage<Coupon> iPage = this.page(page, new LambdaQueryWrapper<Coupon>()
                .eq(Coupon::getIsDelete, 0)
                .orderByAsc(Coupon::getSort)
                .orderByDesc(Coupon::getCreateTime));
        IPage<CouponVo> resultPage = iPage.convert(item -> {
            CouponVo vo = new CouponVo();
            BeanUtils.copyProperties(item, vo);
            vo.setStartTimeText(DateUtil.format(vo.getStartTime(), "YYYY-MM-dd"));
            vo.setEndTimeText(DateUtil.format(vo.getEndTime(), "YYYY-MM-dd"));
            return vo;
        });
        return new Paging(resultPage);
    }

    /**
     * 添加优惠券
     * @param couponParam
     * @return
     */
    public Boolean add(CouponParam couponParam) {
        Coupon coupon = new Coupon();
        //到期类型(10领取后生效 20固定时间)
        if (couponParam.getExpireType() == 10) {
            if (couponParam.getExpireDay() == null) {
                throw new BusinessException("请选择优惠卷的有效天数");
            }
            if(couponParam.getExpireDay() > 3650){
                throw new BusinessException("优惠券持续时间不能超过10年");
            }
        }
        if (couponParam.getExpireType() == 20) {
            if (couponParam.getActiveTime() != null) {
                couponParam.setStartTime(couponParam.getActiveTime()[0]);
                couponParam.setEndTime(couponParam.getActiveTime()[1]);
            } else {
                throw new BusinessException("请选择优惠卷的生效时间");
            }
        }
        BeanUtils.copyProperties(couponParam, coupon);
        //添加商品和分类限制
        this.setLimit(couponParam, coupon);
        return this.save(coupon);
    }

    /**
     * 编辑优惠券
     * @param couponParam
     * @return
     */
    public Boolean edit(CouponParam couponParam) {
        Coupon coupon = new Coupon();
        //到期类型(10领取后生效 20固定时间)
        if (couponParam.getExpireType() == 10) {
            if (couponParam.getExpireDay() == null) {
                throw new BusinessException("请选择优惠卷的有效天数");
            }
            if(couponParam.getExpireDay() > 3650){
                throw new BusinessException("优惠券持续时间不能超过10年");
            }
        }
        if (couponParam.getExpireType() == 20) {
            if (couponParam.getActiveTime() != null) {
                couponParam.setStartTime(couponParam.getActiveTime()[0]);
                couponParam.setEndTime(couponParam.getActiveTime()[1]);
            } else {
                throw new BusinessException("请选择优惠卷的生效时间");
            }
        }
        BeanUtils.copyProperties(couponParam, coupon);
        //添加商品和分类限制
        this.setLimit(couponParam, coupon);
        return this.updateById(coupon);
    }

    /**
     * 设置限制条件
     * @param couponParam
     * @param coupon
     * @return
     */
    private void setLimit(CouponParam couponParam, Coupon coupon) {
        if (couponParam.getApplyRange() == 20) {
            coupon.setProductIds(StringUtils.join(couponParam.getProductIds(), ","));
        }
        if (couponParam.getApplyRange() == 30) {
            JSONObject categoryIds = new JSONObject();
            categoryIds.put("first", couponParam.getCategoryListFirst());
            categoryIds.put("second", couponParam.getCategoryListSecond());
            coupon.setCategoryIds(categoryIds.toJSONString());
        }
    }

    /**
     * 软删除优惠券
     * @param couponId
     * @return
     */
    public Boolean setDelete(Integer couponId) {
        return this.update(new LambdaUpdateWrapper<Coupon>().eq(Coupon::getCouponId, couponId).set(Coupon::getIsDelete, 1));
    }

    /**
     * 获取单个优惠券详情
     * @param couponId
     * @return
     */
    public CouponVo couponDetail(Integer couponId) {
        Coupon coupon = this.getById(couponId);
        CouponVo vo = new CouponVo();
        BeanUtils.copyProperties(coupon, vo);
        // 限制商品
        if (coupon.getApplyRange() == 20) {
            // 商品id
            String[] productIds = coupon.getProductIds().split(",");
            List<Integer> idList = new ArrayList<>();
            for (String str : productIds) {
                idList.add(Integer.parseInt(str));
            }
            vo.setProductShowIds(idList);
            // 商品数据
            vo.setProductList(productService.getListByIds(idList));
        }
        // 限制分类
        if (coupon.getApplyRange() == 30) {
            JSONObject categoryIds = JSON.parseObject(coupon.getCategoryIds());
            JSONObject categoryList = new JSONObject();
            categoryList.put("first", productCategoryService.list(new LambdaQueryWrapper<ProductCategory>()
                    .in(ProductCategory::getCategoryId, categoryIds.getJSONArray("first"))));
            List<ProductCategory> secondList = productCategoryService.list(new LambdaQueryWrapper<ProductCategory>()
                    .in(ProductCategory::getCategoryId, categoryIds.getJSONArray("second")));
            List<CategoryVo> voList = secondList.stream().map(e -> {
                CategoryVo categoryVo = new CategoryVo();
                BeanUtils.copyProperties(e, categoryVo);
                categoryVo.setParent(productCategoryService.getById(e.getParentId()).getName());
                return categoryVo;
            }).collect(Collectors.toList());
            categoryList.put("second", voList);
            vo.setCategoryList(categoryList);
        }
        return vo;
    }

}
