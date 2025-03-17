package net.jjjshop.shop.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.enums.BalanceLogEnum;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.user.UserBalanceLogPageParam;
import net.jjjshop.shop.service.user.UserBalanceLogService;
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
@Api(value = "balanceLog", tags = {"balanceLog"})
@RestController
@RequestMapping("/shop/user/balance")
public class UserBalanceLogController {

    @Autowired
    private UserBalanceLogService userBalanceLogService;

    @RequestMapping(value = "/log", method = RequestMethod.POST)
    @RequiresPermissions("/user/balance/index")
    @OperationLog(name = "log")
    @ApiOperation(value = "log", response = String.class)
    public ApiResult<Map<String, Object>> log(@Validated @RequestBody UserBalanceLogPageParam userBalanceLogPageParam) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("list", userBalanceLogService.getList(userBalanceLogPageParam));
        map.put("scene", BalanceLogEnum.getList());
        return ApiResult.ok(map);
    }
}
