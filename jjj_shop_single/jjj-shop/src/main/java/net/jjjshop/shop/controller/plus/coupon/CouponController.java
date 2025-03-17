package net.jjjshop.shop.controller.plus.coupon;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.UserGrade;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.plus.coupon.CouponPageParam;
import net.jjjshop.shop.param.plus.coupon.CouponParam;
import net.jjjshop.shop.param.plus.coupon.SendCouponParam;
import net.jjjshop.shop.param.plus.coupon.UserCouponPageParam;
import net.jjjshop.shop.service.plus.coupon.CouponService;
import net.jjjshop.shop.service.plus.plus.UserCouponService;
import net.jjjshop.shop.vo.plus.coupon.CouponVo;
import net.jjjshop.shop.vo.user.UserCouponVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(value = "collection", tags = {"collection"})
@RestController
@RequestMapping("/shop/plus/coupon/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;
    @Autowired
    private UserCouponService userCouponService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/plus/coupon/coupon/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Paging<CouponVo>> index(@Validated @RequestBody CouponPageParam couponPageParam) throws Exception {
        return ApiResult.ok(couponService.getList(couponPageParam));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/plus/coupon/coupon/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@Validated @RequestBody CouponParam couponParam) throws Exception {
        if (couponService.add(couponParam)) {
            return ApiResult.ok(null, "添加成功");
        } else {
            return ApiResult.fail("添加失败");
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/plus/coupon/coupon/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@Validated @RequestBody CouponParam couponParam) throws Exception {
        if (couponService.edit(couponParam)) {
            return ApiResult.ok(null, "修改成功");
        } else {
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/plus/coupon/coupon/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> setDelete(Integer id) throws Exception {
        if (couponService.setDelete(id)) {
            return ApiResult.ok(null, "删除成功");
        } else {
            return ApiResult.fail("删除失败");
        }
    }

    @RequestMapping(value = "/couponDetail", method = RequestMethod.POST)
    @RequiresPermissions("/plus/coupon/coupon/edit")
    @OperationLog(name = "couponDetail")
    @ApiOperation(value = "couponDetail", response = String.class)
    public ApiResult<CouponVo> couponDetail(Integer id) {
        return ApiResult.ok(couponService.couponDetail(id));
    }

    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    @RequiresPermissions("/plus/coupon/coupon/receive")
    @OperationLog(name = "receive")
    @ApiOperation(value = "receive", response = String.class)
    public ApiResult<Paging<UserCouponVo>> receive(@Validated @RequestBody UserCouponPageParam userCouponPageParam) {
        return ApiResult.ok(userCouponService.getList(userCouponPageParam));
    }

    @RequestMapping(value = "/toSendCoupon", method = RequestMethod.GET)
    @RequiresPermissions("/plus/coupon/coupon/sendCoupon")
    @OperationLog(name = "toSendCoupon")
    @ApiOperation(value = "toSendCoupon", response = String.class)
    public ApiResult<List<UserGrade>> toSendCoupon() {
        return ApiResult.ok(userCouponService.toSendCoupon());
    }

    @RequestMapping(value = "/sendCoupon", method = RequestMethod.POST)
    @RequiresPermissions("/plus/coupon/coupon/sendCoupon")
    @OperationLog(name = "sendCoupon")
    @ApiOperation(value = "sendCoupon", response = String.class)
    public ApiResult<String> sendCoupon(@Validated @RequestBody SendCouponParam sendCouponParam) {
        if (userCouponService.sendCoupon(sendCouponParam)) {
            return ApiResult.ok(null, "发送成功");
        } else {
            return ApiResult.fail("发送失败");
        }
    }
}
