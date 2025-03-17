package net.jjjshop.front.service.product;

import net.jjjshop.common.entity.product.ProductComment;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.front.param.product.CommentPageParam;
import net.jjjshop.front.param.product.CommentParam;
import net.jjjshop.front.vo.order.OrderCommentVo;
import net.jjjshop.front.vo.product.CommentVo;

import java.util.List;
import java.util.Map;

/**
 * 订单评价记录表 服务类
 * @author jjjshop
 * @since 2022-06-28
 */
public interface ProductCommentService extends BaseService<ProductComment> {

    /**
     * 商品评价分页列表
     * @param commentPageParam
     * @return
     */
    Map<String, Object> getList(CommentPageParam commentPageParam);

    /**
     * 添加商品评论
     * @param params
     * @return
     */
    Boolean add(CommentParam params);

    /**
     * 订单页面评论获取数据方法
     * @param user
     * @param orderId
     * @return
     */
    List<OrderCommentVo> toOrder(User user, Integer orderId);

    /**
     * 查询商品评论数量
     * @param productId
     * @return
     */
    Integer getCommentCount(Integer productId);

    /**
     * 订单详情，显示2条评论
     * @param productId
     * @return
     */
    List<CommentVo> getListForDetail(Integer productId);
}
