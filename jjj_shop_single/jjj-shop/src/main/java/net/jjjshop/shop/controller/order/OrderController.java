

package net.jjjshop.shop.controller.order;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.order.*;
import net.jjjshop.shop.service.order.OrderService;
import net.jjjshop.shop.vo.order.OrderVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "订单管理", tags = {"订单管理"})
@RestController
@RequestMapping("/shop/order/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/order/order/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String,Object>> index(@Validated @RequestBody OrderPageParam orderPageParam) throws Exception{
        Map<String,Object> result = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("payment", orderService.getCount("payment"));
        jsonObject.put("delivery", orderService.getCount("delivery"));
        jsonObject.put("received", orderService.getCount("received"));
        result.put("orderList", orderService.getList(orderPageParam));
        result.put("orderCount", jsonObject);
        result.put("deliveryList", orderService.getDeliveryList());
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @RequiresPermissions("/order/order/detail")
    @OperationLog(name = "detail")
    @ApiOperation(value = "detail", response = String.class)
    public ApiResult<OrderVo> detail(Integer orderId) throws Exception{
        return ApiResult.ok(orderService.detail(orderId));
    }

    //修改订单价格
    @RequestMapping(value = "/updatePrice", method = RequestMethod.POST)
    @RequiresPermissions("/order/order/updatePrice")
    @OperationLog(name = "updatePrice")
    @ApiOperation(value = "updatePrice", response = String.class)
    public ApiResult<String> updatePrice(@Validated @RequestBody OrderPriceParam orderPriceParam){
        if (orderService.updatePrice(orderPriceParam)) {
            return ApiResult.ok(null, "修改成功");
        } else {
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/delivery", method = RequestMethod.POST)
    @RequiresPermissions("/order/order/delivery")
    @OperationLog(name = "delivery")
    @ApiOperation(value = "delivery", response = String.class)
    public ApiResult<String> delivery(@Validated @RequestBody OrderParam orderParam) throws Exception{
        if (orderService.delivery(orderParam)) {
            return ApiResult.ok(null, "发货成功");
        } else {
            return ApiResult.fail("发货失败");
        }
    }

    @RequestMapping(value = "/orderCancel", method = RequestMethod.POST)
    @RequiresPermissions("/order/operate/confirmCancel")
    @OperationLog(name = "orderCancel")
    @ApiOperation(value = "orderCancel", response = String.class)
    public ApiResult<String> orderCancel(@Validated @RequestBody OrderCancelParam orderCancelParam){
        if (orderService.orderCancel(orderCancelParam)) {
            return ApiResult.ok(null, "取消成功");
        } else {
            return ApiResult.fail("取消失败");
        }
    }

    @RequestMapping(value = "/virtual", method = RequestMethod.POST)
    @RequiresPermissions("/order/order/delivery")
    @OperationLog(name = "virtual")
    @ApiOperation(value = "virtual", response = String.class)
    public ApiResult<String> virtual(@Validated @RequestBody OrderVirtualParam orderVirtualParam){
        if (orderService.virtual(orderVirtualParam)) {
            return ApiResult.ok(null, "发货成功");
        } else {
            return ApiResult.fail("发货失败");
        }
    }
}
