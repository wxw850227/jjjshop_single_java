package net.jjjshop.shop.controller.store;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.store.StoreClerkPageParam;
import net.jjjshop.shop.param.store.StoreClerkParam;
import net.jjjshop.shop.service.store.StoreClerkService;
import net.jjjshop.shop.service.store.StoreService;
import net.jjjshop.shop.vo.store.StoreClerkVo;
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
@Api(value = "index", tags = {"index"})
@RestController
@RequestMapping("/shop/store/clerk")
public class StoreClerkController {

    @Autowired
    private StoreClerkService storeClerkService;

    @Autowired
    private StoreService storeService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/store/clerk/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Paging<StoreClerkVo>> index(@Validated @RequestBody StoreClerkPageParam storeClerkPageParam) throws Exception {
        return ApiResult.ok(storeClerkService.getList(storeClerkPageParam));
    }

    @RequestMapping(value = "/toAdd", method = RequestMethod.GET)
    @RequiresPermissions("/store/clerk/add")
    @OperationLog(name = "toAdd")
    @ApiOperation(value = "toAdd", response = String.class)
    public ApiResult<Map<String, Object>> toAdd() throws Exception {
        return ApiResult.ok(storeClerkService.toAdd());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/store/clerk/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@Validated @RequestBody StoreClerkParam storeClerkParam) throws Exception {
        if(storeClerkService.add(storeClerkParam)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    @RequestMapping(value = "/toEdit", method = RequestMethod.GET)
    @RequiresPermissions("/store/clerk/edit")
    @OperationLog(name = "toEdit")
    @ApiOperation(value = "toEdit", response = String.class)
    public ApiResult<Map<String, Object>> toEdit(Integer clerkId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("storeList", storeService.getAll());
        map.put("detail", storeClerkService.toEdit(clerkId));
        return ApiResult.ok(map);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/store/clerk/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@Validated @RequestBody StoreClerkParam storeClerkParam) throws Exception {
        if(storeClerkService.edit(storeClerkParam)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/store/clerk/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Integer clerkId) throws Exception {
        if(storeClerkService.setDelete(clerkId)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }
}
