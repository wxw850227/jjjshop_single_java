

package net.jjjshop.front.controller.user;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.service.user.UserGradeService;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.front.controller.BaseController;
import net.jjjshop.front.service.order.OrderService;
import net.jjjshop.front.service.page.CenterMenuService;
import net.jjjshop.front.service.user.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "user", tags = {"用户首页"})
@RestController
@RequestMapping("/front/user/index")
public class IndexController extends BaseController {

    @Autowired
    private CenterMenuService centerMenuService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserGradeService userGradeService;
    @Autowired
    private UserCouponService userCouponService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index(){
        Map<String, Object> result = new HashMap<>();
        User user = this.getUser(true);
        result.put("user", user);
        result.put("userGrade", userGradeService.getById(user.getGradeId()).getName());
        result.put("menus", centerMenuService.getMenus());
        // 订单数量
        JSONObject orderCount = new JSONObject();
        orderCount.put("payment", orderService.getCount(user.getUserId(), "payment"));
        orderCount.put("delivery", orderService.getCount(user.getUserId(), "delivery"));
        orderCount.put("received", orderService.getCount(user.getUserId(), "received"));
        orderCount.put("comment", orderService.getCount(user.getUserId(), "comment"));
        result.put("orderCount", orderCount);
        // 优惠券
        result.put("coupon", userCouponService.getUserCouponCount(user.getUserId()));
        // 获取手机号
        result.put("getPhone", false);
        // 设置项
        JSONObject setting = new JSONObject();
        result.put("setting", setting);
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/setting", method = RequestMethod.GET)
    @OperationLog(name = "setting")
    @ApiOperation(value = "setting", response = String.class)
    public ApiResult<Map<String, Object>> setting(){
        Map<String, Object> result = new HashMap<>();
        User user = this.getUser(true);
        result.put("userInfo", user);
        return ApiResult.ok(result);
    }
}
