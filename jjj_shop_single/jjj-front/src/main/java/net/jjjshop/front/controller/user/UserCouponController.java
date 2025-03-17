package net.jjjshop.front.controller.user;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.front.controller.BaseController;
import net.jjjshop.front.param.user.UserCouponPageParam;
import net.jjjshop.front.service.user.UserCouponService;
import net.jjjshop.front.vo.coupon.UserCouponVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(value = "user", tags = {"用户优惠券"})
@RestController
@RequestMapping("/front/user/coupon")
public class UserCouponController extends BaseController {

    @Autowired
    private UserCouponService userCouponService;


    @RequestMapping(value = "/receive", method = RequestMethod.POST)
    @OperationLog(name = "receive")
    @ApiOperation(value = "receive", response = String.class)
    public ApiResult<String> receive(Integer couponId) {
        if (userCouponService.receive(this.getUser(true), couponId)) {
            return ApiResult.ok(null, "添加成功");
        } else {
            return ApiResult.fail("添加失败");
        }
    }

    @RequestMapping(value = "/lists", method = RequestMethod.POST)
    @OperationLog(name = "lists")
    @ApiOperation(value = "lists", response = String.class)
    public ApiResult<Paging<UserCouponVo>> lists(@Validated @RequestBody UserCouponPageParam userCouponPageParam) {
        return ApiResult.ok(userCouponService.getPageList(this.getUser(true).getUserId(),userCouponPageParam));
    }

    @RequestMapping(value = "/receiveList", method = RequestMethod.GET)
    @OperationLog(name = "receiveList")
    @ApiOperation(value = "receiveList", response = String.class)
    public ApiResult<String> receiveList(String couponIds) {
        //couponIds = couponIds.substring(1,couponIds.length()-1);
        List<Integer> list=JSONObject.parseArray(couponIds, Integer.class);
        if(userCouponService.receiveList(this.getUser(true), list)){
            return ApiResult.ok(null, "领取成功");
        } else {
            return ApiResult.fail("领取失败");
        }
    }

}
