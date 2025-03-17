package net.jjjshop.shop.service.product;

import net.jjjshop.common.entity.product.ProductComment;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.shop.param.product.CommentPageParam;
import net.jjjshop.shop.param.product.CommentParam;
import net.jjjshop.shop.vo.product.CommentVo;

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
     * 商品评价详情
     * @param commentId
     * @return
     */
    CommentVo getDetail(Integer commentId);

    /**
     * 修改商品评价
     * @param commentParam
     * @return
     */
    Boolean edit(CommentParam commentParam);

    /**
     * 软删除商品评价
     * @param commentId
     * @return
     */
    Boolean setDelete(Integer commentId);

    /**
     * 商品评价总数
     * @param
     * @return
     */
    Integer getProductCommentTotal();

    /**
     * 商品浏览总数
     * @param
     * @return
     */
    Integer getReviewCommentTotal();
}
