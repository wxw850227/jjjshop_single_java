package net.jjjshop.front.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.front.controller.BaseController;
import net.jjjshop.front.param.order.OrderRefundDeliveryParam;
import net.jjjshop.front.param.order.OrderRefundPageParam;
import net.jjjshop.front.param.order.OrderRefundParam;
import net.jjjshop.front.service.order.OrderProductService;
import net.jjjshop.front.service.order.OrderRefundService;
import net.jjjshop.front.service.user.UserOrderService;
import net.jjjshop.front.vo.order.OrderRefundApplyVo;
import net.jjjshop.front.vo.order.OrderRefundDetailVo;
import net.jjjshop.front.vo.order.OrderRefundListVo;
import net.jjjshop.front.vo.settings.ExpressDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "user", tags = {"用户售后订单管理"})
@RestController
@RequestMapping("/front/user/refund")
public class UserRefundController extends BaseController {

    @Autowired
    private OrderRefundService orderRefundService;
    @Autowired
    private UserOrderService userOrderService;
    @Autowired
    private OrderProductService orderProductService;

    @RequestMapping(value = "/lists", method = RequestMethod.POST)
    @OperationLog(name = "lists")
    @ApiOperation(value = "lists", response = String.class)
    public ApiResult<Paging<OrderRefundListVo>> lists(@RequestBody OrderRefundPageParam orderRefundPageParam){
        orderRefundPageParam.setUserId(this.getUser(true).getUserId());
        return ApiResult.ok(orderRefundService.getList(orderRefundPageParam));
    }

    @RequestMapping(value = "/apply", method = RequestMethod.POST)
    @OperationLog(name = "apply")
    @ApiOperation(value = "apply", response = String.class)
    public ApiResult<String> apply(@RequestBody OrderRefundParam orderRefundParam){
        if(orderRefundService.apply(orderRefundParam)){
            return ApiResult.ok(null, "申请售后成功");
        }else{
            return ApiResult.fail("申请售后失败");
        }
    }

    @RequestMapping(value = "/toApply", method = RequestMethod.GET)
    @OperationLog(name = "toApply")
    @ApiOperation(value = "toApply", response = String.class)
    public ApiResult<OrderRefundApplyVo> toApply(Integer orderProductId){
        return ApiResult.ok(orderRefundService.toApply(orderProductId));
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @OperationLog(name = "detail")
    @ApiOperation(value = "detail", response = String.class)
    public ApiResult<OrderRefundDetailVo> detail(Integer orderRefundId){
        return ApiResult.ok(orderRefundService.detail(orderRefundId));
    }

    @RequestMapping(value = "/delivery", method = RequestMethod.POST)
    @OperationLog(name = "delivery")
    @ApiOperation(value = "delivery", response = String.class)
    public ApiResult<String> delivery(@RequestBody OrderRefundDeliveryParam orderRefundDeliveryParam){
        if(orderRefundService.delivery(orderRefundDeliveryParam)){
            return ApiResult.ok(null, "售后发货成功");
        }else{
            return ApiResult.fail("售后发货失败");
        }
    }

    @RequestMapping(value = "/express", method = RequestMethod.GET)
    @OperationLog(name = "express")
    @ApiOperation(value = "express", response = String.class)
    public ApiResult<ExpressDetailVo> express(Integer orderRefundId)  throws Exception  {
        return ApiResult.ok(orderRefundService.express(orderRefundId));
    }


}
