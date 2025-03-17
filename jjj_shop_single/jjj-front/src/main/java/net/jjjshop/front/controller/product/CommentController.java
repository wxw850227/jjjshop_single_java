package net.jjjshop.front.controller.product;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.ProductComment;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.front.controller.BaseController;
import net.jjjshop.front.param.product.CommentPageParam;
import net.jjjshop.front.param.product.ProductParam;
import net.jjjshop.front.service.product.ProductCommentService;
import net.jjjshop.front.vo.product.CommentVo;
import net.jjjshop.front.vo.product.ProductListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@Api(value = "productComment", tags = {"商品评价"})
@RestController
@RequestMapping("/front/product/comment")
public class CommentController extends BaseController {

    @Autowired
    private ProductCommentService productCommentService;

    @RequestMapping(value = "/lists", method = RequestMethod.POST)
    @OperationLog(name = "lists")
    @ApiOperation(value = "lists", response = String.class)
    public ApiResult<Map<String, Object>> lists(@Validated @RequestBody CommentPageParam commentPageParam) {
        return ApiResult.ok(productCommentService.getList(commentPageParam));
    }
}
