package net.jjjshop.front.controller.store;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.store.StoreClerk;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.front.controller.BaseController;
import net.jjjshop.front.service.order.OrderService;
import net.jjjshop.front.service.store.StoreClerkService;
import net.jjjshop.front.vo.order.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "store", tags = {"订单核销"})
@RestController
@RequestMapping("/front/store/order")
public class OrderController extends BaseController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private StoreClerkService storeClerkService;

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @OperationLog(name = "detail")
    @ApiOperation(value = "detail", response = String.class)
    public ApiResult<OrderDetailVo> detail(String orderNo) throws Exception{
        OrderDetailVo orderDetailVo = orderService.detailByNo(orderNo);
        return ApiResult.ok(orderDetailVo);
    }

    @RequestMapping(value = "/extract", method = RequestMethod.POST)
    @OperationLog(name = "extract")
    @ApiOperation(value = "extract", response = String.class)
    public ApiResult<String> extract(Integer orderId) throws Exception{
        OrderDetailVo orderDetailVo = orderService.detail(orderId);
        //验证当前用户是否是门店店员
        User user = this.getUser(true);
        StoreClerk storeClerk = storeClerkService.detail(orderDetailVo.getExtractStoreId(), user.getUserId());
        if(orderService.verificationOrder(orderId, storeClerk.getClerkId())){
            return ApiResult.ok(null, "订单核销成功");
        }else{
            return ApiResult.fail("订单核销失败");
        }
    }
}
