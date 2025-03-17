package net.jjjshop.shop.service.product.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.OrderProduct;
import net.jjjshop.common.entity.product.ProductComment;
import net.jjjshop.common.entity.product.ProductCommentImage;
import net.jjjshop.common.mapper.product.ProductCommentMapper;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.product.CommentPageParam;
import net.jjjshop.shop.param.product.CommentParam;
import net.jjjshop.shop.service.order.OrderProductService;
import net.jjjshop.shop.service.order.OrderService;
import net.jjjshop.shop.service.product.ProductCommentImageService;
import net.jjjshop.shop.service.product.ProductCommentService;
import net.jjjshop.shop.service.user.UserService;
import net.jjjshop.shop.vo.product.CommentVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 订单评价记录表 服务实现类
 * @author jjjshop
 * @since 2022-06-28
 */
@Slf4j
@Service
public class ProductCommentServiceImpl extends BaseServiceImpl<ProductCommentMapper, ProductComment> implements ProductCommentService {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderProductService orderProductService;
    @Autowired
    private UploadFileUtils uploadFileUtils;
    @Autowired
    private ProductCommentImageService productCommentImageService;

    /**
     * 商品评价分页列表
     * @param commentPageParam
     * @return
     */
    public Map<String, Object> getList(CommentPageParam commentPageParam) {
        Map<String, Object> map = new HashMap<>();
        Page<ProductComment> page = new PageInfo<>(commentPageParam);
        LambdaQueryWrapper<ProductComment> wrapper = new LambdaQueryWrapper<>();
        if (commentPageParam.getScore() != null && commentPageParam.getScore() != 0) {
            wrapper.eq(ProductComment::getScore, commentPageParam.getScore());
        }
        if (commentPageParam.getStatus() != -1) {
            wrapper.eq(ProductComment::getStatus, commentPageParam.getStatus());
        }
        if (StringUtils.isNotEmpty(commentPageParam.getName())) {
            List<OrderProduct> list = orderProductService.list(new LambdaQueryWrapper<OrderProduct>().like(OrderProduct::getProductName, commentPageParam.getName()));
            if (list != null && list.size() > 0) {
                List<Integer> productIds = list.stream().map(e -> {
                    return e.getProductId();
                }).collect(Collectors.toList());
                wrapper.in(ProductComment::getProductId, productIds);
            } else {
                wrapper.eq(ProductComment::getProductId, -1);
            }
        }
        wrapper.eq(ProductComment::getIsDelete, 0);
        wrapper.orderByAsc(ProductComment::getSort);
        wrapper.orderByDesc(ProductComment::getCreateTime);
        IPage<ProductComment> iPage = this.page(page, wrapper);
        IPage<CommentVo> result = iPage.convert(item -> {
            OrderProduct orderProduct = orderProductService.getById(item.getOrderProductId());
            CommentVo vo = new CommentVo();
            BeanUtils.copyProperties(item, vo);
            vo.setProductName(orderProduct.getProductName());
            vo.setProductPrice(orderProduct.getProductPrice());
            vo.setImagePath(uploadFileUtils.getFilePath(orderProduct.getImageId()));
            vo.setOrderNo(orderService.getById(item.getOrderId()).getOrderNo());
            vo.setNickname(userService.getById(item.getUserId()).getNickname());
            return vo;
        });
        Integer num = this.getNum();
        map.put("comments", new Paging(result));
        map.put("num", num);
        return map;
    }

    /**
     * 获取待审核商品数量
     * @param
     * @return
     */
    private Integer getNum() {
        return this.count(new LambdaQueryWrapper<ProductComment>().eq(ProductComment::getIsDelete, 0).eq(ProductComment::getStatus, 0));
    }

    /**
     * 商品评价详情
     * @param commentId
     * @return
     */
    public CommentVo getDetail(Integer commentId) {
        ProductComment comment = this.getById(commentId);
        CommentVo vo = new CommentVo();
        BeanUtils.copyProperties(comment, vo);
        OrderProduct orderProduct = orderProductService.getById(comment.getOrderProductId());
        vo.setProductName(orderProduct.getProductName());
        vo.setProductPrice(orderProduct.getProductPrice());
        vo.setImagePath(uploadFileUtils.getFilePath(orderProduct.getImageId()));
        vo.setOrderNo(orderService.getById(comment.getOrderId()).getOrderNo());
        vo.setNickname(userService.getById(comment.getUserId()).getNickname());
        if (vo.getIsPicture() == 1) {
            vo.setImage(productCommentImageService.list(new LambdaQueryWrapper<ProductCommentImage>()
                            .eq(ProductCommentImage::getCommentId, comment.getCommentId())
                            .orderByAsc(ProductCommentImage::getId))
                    .stream().map(e -> {
                        return uploadFileUtils.getFilePath(e.getImageId());
                    }).collect(Collectors.toList()));
        }
        return vo;
    }

    /**
     * 修改商品评价
     * @param commentParam
     * @return
     */
    public Boolean edit(CommentParam commentParam){
        ProductComment comment = this.getById(commentParam.getCommentId());
        BeanUtils.copyProperties(commentParam, comment);
        return this.updateById(comment);
    }

    /**
     * 软删除商品评价
     * @param commentId
     * @return
     */
    public Boolean setDelete(Integer commentId){
        return this.update(new LambdaUpdateWrapper<ProductComment>().eq(ProductComment::getCommentId, commentId).set(ProductComment::getIsDelete, 1));
    }


    /**
     * 商品评价总数
     * @param
     * @return
     */
    public Integer getProductCommentTotal() {
        return this.count(new LambdaQueryWrapper<ProductComment>().eq(ProductComment::getIsDelete, 0));
    }

    /**
     * 商品浏览总数
     * @param
     * @return
     */
    public Integer getReviewCommentTotal() {
        return this.count(new LambdaQueryWrapper<ProductComment>()
                .eq(ProductComment::getIsDelete, 0)
                .eq(ProductComment::getStatus, 0));
    }
}
