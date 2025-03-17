package net.jjjshop.shop.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.user.UserGradeLogPageParam;
import net.jjjshop.shop.service.user.UserGradeLogService;
import net.jjjshop.shop.vo.user.UserGradeLogVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "gradeLog", tags = {"gradeLog"})
@RestController
@RequestMapping("/shop/user/grade")
public class UserGradeLogController {

    @Autowired
    private UserGradeLogService userGradeLogService;

    @RequestMapping(value = "/log", method = RequestMethod.POST)
    @RequiresPermissions("/user/grade/log")
    @OperationLog(name = "log")
    @ApiOperation(value = "log", response = String.class)
    public ApiResult<Paging<UserGradeLogVo>> log(@Validated @RequestBody UserGradeLogPageParam userGradeLogPageParam) throws Exception {
        return ApiResult.ok(userGradeLogService.getList(userGradeLogPageParam));
    }
}
