

package net.jjjshop.shop.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.service.shop.ShopUserService;
import net.jjjshop.shop.service.statistics.HomeDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "index", tags = {"index"})
@RestController
@RequestMapping("/shop/index")
public class IndexController {

    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private ShopUserService shopUserService;
    @Autowired
    private HomeDataService homeDataService;

    @RequestMapping(value = "/base", method = RequestMethod.POST)
    @OperationLog(name = "base")
    @ApiOperation(value = "base", response = String.class)
    public ApiResult<Map<String, String>> base() throws Exception{
        JSONObject vo = settingUtils.getSetting(SettingEnum.SYS_CONFIG.getKey(), null);
        Map<String, String> result = new HashMap<String, String>();
        result.put("shopName", vo.getString("shopName"));
        result.put("shopBgImg", vo.getString("shopBgImg"));
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<JSONObject> index() throws Exception{
        return ApiResult.ok(homeDataService.getHomeData());
    }
}
