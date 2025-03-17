

package net.jjjshop.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.admin.service.PlusCategoryService;
import net.jjjshop.admin.service.ShopAccessService;
import net.jjjshop.admin.vo.PlusCategoryVo;
import net.jjjshop.common.entity.shop.ShopAccess;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(value = "test", tags = {"test"})
@RestController
@RequestMapping("/admin/plus")
public class PlusController {

    @Autowired
    private PlusCategoryService plusCategoryService;
    @Autowired
    private ShopAccessService shopAccessService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<List<PlusCategoryVo>> index() {
        List<PlusCategoryVo> list = plusCategoryService.getAll();
        return ApiResult.ok(list);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<Boolean> delete(Integer id) {
        Boolean result = shopAccessService.editPlusCategoryId(id, 0);
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<List<ShopAccess>> toAdd() {
        List<ShopAccess> list = shopAccessService.getAllPlus();
        return ApiResult.ok(list);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<Boolean> add(Integer id,Integer plusCategoryId) {
        Boolean result = shopAccessService.editPlusCategoryId(id, plusCategoryId);
        return ApiResult.ok(result);
    }
}
