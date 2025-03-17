package net.jjjshop.shop.controller.data;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.CommonPageParam;
import net.jjjshop.shop.service.store.StoreService;
import net.jjjshop.shop.vo.store.StoreVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Api(value = "门店数据", tags = {"门店数据"})
@RestController
@RequestMapping("/shop/data/store")
public class StoreDataController {

    @Autowired
    private StoreService storeService;

    @RequestMapping(value = "/lists", method = RequestMethod.POST)
    @OperationLog(name = "lists")
    @ApiOperation(value = "lists", response = String.class)
    public ApiResult<Paging<StoreVo>> lists(@Validated @RequestBody CommonPageParam commonPageParam) throws Exception{
        return ApiResult.ok(storeService.getList(commonPageParam));
    }
}
