

package net.jjjshop.front.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.framework.shiro.util.JwtTokenUtil;
import net.jjjshop.front.controller.BaseController;
import net.jjjshop.front.param.user.PhoneRegisterParam;
import net.jjjshop.front.service.user.UserService;
import net.jjjshop.front.vo.user.LoginUserTokenVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(value = "user", tags = {"index"})
@RestController
@RequestMapping("/front/user/userOpen")
public class UserOpenController extends BaseController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/phoneLogin", method = RequestMethod.POST)
    @OperationLog(name = "phoneLogin")
    @ApiOperation(value = "phoneLogin", response = String.class)
    public ApiResult<LoginUserTokenVo> phoneLogin(@RequestParam String mobile, @RequestParam String password){
        log.debug("phoneLogin...");
        LoginUserTokenVo user = userService.login(mobile, password);
        // 设置token响应头
        response.setHeader(JwtTokenUtil.getTokenName(""), user.getToken());
        return ApiResult.ok(user, "登录成功");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @OperationLog(name = "register")
    @ApiOperation(value = "register", response = String.class)
    public ApiResult<String> register(@RequestBody PhoneRegisterParam phoneRegisterParam){
        if(userService.phoneRegister(phoneRegisterParam)){
            return ApiResult.ok(null, "注册成功");
        }else {
            return ApiResult.fail("注册失败");
        }
    }

}
