package net.jjjshop.shop.controller.setting;


import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.service.settings.PrinterService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(value = "printing", tags = {"printing"})
@RestController
@RequestMapping("/shop/setting/printing")
public class PrintingController {
    @Autowired
    private PrinterService printerService;

    @Autowired
    private SettingUtils settingUtils;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @RequiresPermissions("/setting/printing/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Map<String, Object>> index() throws Exception {
        JSONObject vo = settingUtils.getSetting(SettingEnum.PRINTING.getKey(), null);
        Map<String, Object> result = new HashMap<>();
        result.put("printerList", printerService.getAll());
        result.put("isOpen", vo.get("isOpen"));
        result.put("printerId", vo.get("printerId"));
        result.put("orderStatus", vo.get("orderStatus"));
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/setting/printing/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<String> index(@RequestBody JSONObject jsonData) throws Exception {
        settingUtils.saveSetting(SettingEnum.PRINTING.getKey(), jsonData);
        return ApiResult.ok("保存成功");
    }
}
