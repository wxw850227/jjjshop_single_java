package net.jjjshop.shop.controller.order;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.order.OrderExtractParam;
import net.jjjshop.shop.param.order.OrderPageParam;
import net.jjjshop.shop.param.order.OrderParam;
import net.jjjshop.shop.service.order.OrderService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@Api(value = "订单管理", tags = {"订单管理"})
@RestController
@RequestMapping("/shop/order/operate")
public class OrderOperateController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/confirmCancel", method = RequestMethod.POST)
    @RequiresPermissions("/order/operate/confirmCancel")
    @OperationLog(name = "confirmCancel")
    @ApiOperation(value = "confirmCancel", response = String.class)
    public ApiResult<String> confirmCancel(@Validated @RequestBody OrderParam orderParam) throws Exception{
        if(orderService.confirmCancel(orderParam)){
            return ApiResult.ok(null, "审核成功");
        }else{
            return ApiResult.fail("审核失败");
        }
    }

    @RequestMapping(value = "/export", method = RequestMethod.GET)
    @RequiresPermissions("/order/operate/export")
    @OperationLog(name = "export")
    @ApiOperation(value = "export", response = String.class)
    public ApiResult<String> export(String dataType, HttpServletResponse httpServletResponse) throws Exception{
        OrderPageParam orderPageParam = new OrderPageParam();
        orderPageParam.setDataType(dataType);
        orderService.exportList(orderPageParam, httpServletResponse);
            return ApiResult.ok(null, "订单导出成功");
    }

    @RequestMapping(value = "/extract", method = RequestMethod.POST)
    @RequiresPermissions("/order/operate/extract")
    @OperationLog(name = "extract")
    @ApiOperation(value = "extract", response = String.class)
    public ApiResult<String> extract(@Validated @RequestBody OrderExtractParam orderExtractParam) throws Exception{
        if(orderService.verificationOrder(orderExtractParam)){
            return ApiResult.ok(null, "订单核销成功");
        }else {
            return ApiResult.fail("订单核销失败");
        }
    }

}
