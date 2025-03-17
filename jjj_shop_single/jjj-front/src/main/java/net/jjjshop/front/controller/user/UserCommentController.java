package net.jjjshop.front.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.front.controller.BaseController;
import net.jjjshop.front.param.product.CommentParam;
import net.jjjshop.front.service.product.ProductCommentService;
import net.jjjshop.front.vo.order.OrderCommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(value = "user", tags = {"用户评价"})
@RestController
@RequestMapping("/front/user/comment")
public class UserCommentController extends BaseController {

    @Autowired
    private ProductCommentService productCommentService;

    @RequestMapping(value = "/toOrder", method = RequestMethod.GET)
    @OperationLog(name = "toOrder")
    @ApiOperation(value = "toOrder", response = String.class)
    public ApiResult<List<OrderCommentVo>> toOrder(Integer orderId) {
        return ApiResult.ok(productCommentService.toOrder(this.getUser(true), orderId));
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    @OperationLog(name = "order")
    @ApiOperation(value = "order", response = String.class)
    public ApiResult<String> order(@RequestBody CommentParam params) {
        if(productCommentService.add(params)){
            return ApiResult.ok(null, "评价成功");
        }else{
            return ApiResult.fail("评价失败");
        }
    }
}
