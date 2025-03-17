package net.jjjshop.front.controller.store;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.front.controller.BaseController;
import net.jjjshop.front.service.store.StoreService;
import net.jjjshop.front.vo.store.StoreVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(value = "store", tags = {"门店列表"})
@RestController
@RequestMapping("/front/store/store")
public class StoreController extends BaseController {
    @Autowired
    private StoreService storeService;

    @RequestMapping(value = "/lists", method = RequestMethod.POST)
    @OperationLog(name = "lists")
    @ApiOperation(value = "lists", response = String.class)
    public ApiResult<List<StoreVo>> recommendProduct(String longitude, String latitude) throws Exception{
        return ApiResult.ok(storeService.getAllCheck(longitude, latitude));
    }
}
