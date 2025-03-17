

package net.jjjshop.front.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.front.service.page.PageService;
import net.jjjshop.front.service.plus.coupon.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"index"})
@RestController
@RequestMapping("/front/index")
public class IndexController extends BaseController {

    @Autowired
    private PageService pageService;
    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private CouponService couponService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index(Integer pageId) {
        Map<String, Object> result = new HashMap<>();
        // 页面元素
        User user = this.getUser(false);
        result.put("page", pageService.getPageData(user, pageId));
        // 设置项
        Map<String, Object> setting = new HashMap<>();
        setting.put("collection", settingUtils.getSetting(SettingEnum.COLLECTION.getKey(), null));
        setting.put("homepush", pageService.getHomePush());
        setting.put("officia", settingUtils.getSetting(SettingEnum.OFFICIA.getKey(), null));
        result.put("setting", setting);
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/nav", method = RequestMethod.GET)
    @OperationLog(name = "nav")
    @ApiOperation(value = "nav", response = String.class)
    public ApiResult<Map<String, Object>> nav() {
        Map<String, Object> result = new HashMap<>();
        result.put("theme", settingUtils.getSetting(SettingEnum.THEME.getKey(), null));
        result.put("nav", settingUtils.getSetting(SettingEnum.NAV.getKey(), null));
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/diy", method = RequestMethod.GET)
    @OperationLog(name = "diy")
    @ApiOperation(value = "diy", response = String.class)
    public ApiResult<Map<String, Object>> diy(Integer pageId) {
        Map<String, Object> result = new HashMap<>();
        // 页面元素
        User user = this.getUser(false);
        result.put("page", pageService.getPageData(user, pageId));
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/mpService", method = RequestMethod.GET)
    @OperationLog(name = "mpService")
    @ApiOperation(value = "mpService", response = String.class)
    public ApiResult<JSONObject> mpService(Long appId) {
        JSONObject jsonObject = settingUtils.getSetting(SettingEnum.MPSERVICE.getKey(), appId);
        return ApiResult.ok(jsonObject);
    }
}
