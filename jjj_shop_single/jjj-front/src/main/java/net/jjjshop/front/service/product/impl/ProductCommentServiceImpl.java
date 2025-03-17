package net.jjjshop.front.service.product.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.product.ProductComment;
import net.jjjshop.common.entity.product.ProductCommentImage;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.mapper.product.ProductCommentMapper;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.front.param.product.CommentListParam;
import net.jjjshop.front.param.product.CommentPageParam;
import net.jjjshop.front.param.product.CommentParam;
import net.jjjshop.front.service.order.OrderProductService;
import net.jjjshop.front.service.order.OrderService;
import net.jjjshop.front.service.product.ProductCommentImageService;
import net.jjjshop.front.service.product.ProductCommentService;
import net.jjjshop.front.service.user.UserService;
import net.jjjshop.front.vo.order.OrderCommentVo;
import net.jjjshop.front.vo.product.CommentVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private UploadFileUtils uploadFileUtils;
    @Autowired
    private ProductCommentImageService productCommentImageService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderProductService orderProductService;

    /**
     * 商品评价分页列表
     * @param commentPageParam
     * @return
     */
    public Map<String, Object> getList(CommentPageParam commentPageParam) {
        Map<String, Object> map = new HashMap<>();
        Page<ProductComment> page = new PageInfo<>(commentPageParam);
        LambdaQueryWrapper<ProductComment> wrapper = new LambdaQueryWrapper<>();
        if (commentPageParam.getScore() != null && commentPageParam.getScore() != -1) {
            wrapper.eq(ProductComment::getScore, commentPageParam.getScore());
        }
        Integer productId = commentPageParam.getProductId();
        wrapper.eq(ProductComment::getProductId, productId);
        wrapper.eq(ProductComment::getIsDelete, 0);
        wrapper.eq(ProductComment::getStatus, 1);
        wrapper.orderByAsc(ProductComment::getSort);
        wrapper.orderByDesc(ProductComment::getCreateTime);
        IPage<ProductComment> iPage = this.page(page, wrapper);
        IPage<CommentVo> result = iPage.convert(item -> {
            return this.transVo(item);
        });
        JSONObject total = new JSONObject();
        total.put("all", this.getTotal(productId, 0));
        total.put("negative", this.getTotal(productId, 30));
        total.put("praise", this.getTotal(productId, 10));
        total.put("review", this.getTotal(productId, 20));
        map.put("comments", new Paging(result));
        map.put("total", total);
        return map;
    }

    /**
     * 获取待审核商品数量
     * @param productId
     * @param score
     * @return
     */
    private Integer getTotal(Integer productId, Integer score) {
        LambdaQueryWrapper<ProductComment> wrapper = new LambdaQueryWrapper<ProductComment>().eq(ProductComment::getProductId, productId).eq(ProductComment::getIsDelete, 0);
        wrapper.eq(ProductComment::getStatus, 1);
        if(score != 0){
            wrapper.eq(ProductComment::getScore, score);
            return this.count(wrapper);
        }
        return this.count(wrapper);
    }

    /**
     * 添加商品评论
     * @param params
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(CommentParam params){
        List<CommentListParam> list = params.getParams();
        orderService.update(new LambdaUpdateWrapper<Order>().eq(Order::getOrderId, params.getOrderId()).set(Order::getIsComment, 1));
        params.getParams().stream().forEach(e->{
            ProductComment comment = new ProductComment();
            BeanUtils.copyProperties(e, comment);
            if(CollectionUtils.isNotEmpty(e.getImageList())){
                comment.setIsPicture(1);
            }
            if(StringUtils.isEmpty(comment.getContent())){
                throw new BusinessException("请输入文字评价");
            }
            boolean save = this.save(comment);
            this.saveCommentImage(e, comment);
        });
        return true;
    }

    /**
     * 保存商品评论图片
     * @param commentListParam
     * @param comment
     * @return
     */
    private void saveCommentImage(CommentListParam commentListParam, ProductComment comment){
        List<ProductCommentImage> result = commentListParam.getImageList().stream().map(e -> {
            ProductCommentImage image = new ProductCommentImage();
            image.setImageId(e.getFileId());
            image.setCommentId(comment.getCommentId());
            return image;
        }).collect(Collectors.toList());
        productCommentImageService.saveBatch(result);
    }

    /**
     * 订单页面评论获取数据方法
     * @param user
     * @param orderId
     * @return
     */
    public List<OrderCommentVo> toOrder(User user, Integer orderId) {
        Order order = orderService.getById(orderId);
        if(order.getOrderStatus() != 30){
            throw new BusinessException("该订单未完成，无法评价");
        }
        if(order.getIsComment() == 1){
            throw new BusinessException("该订单已完成评价");
        }
        return orderProductService.getProductVoList(orderId).stream().map(e -> {
            OrderCommentVo vo = new OrderCommentVo();
            BeanUtils.copyProperties(e, vo);
            vo.setNickname(user.getNickname());
            vo.setProductImage(uploadFileUtils.getFilePath(e.getImageId()));
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 查询商品评论数量
     * @param productId
     * @return
     */
    public Integer getCommentCount(Integer productId){
        return this.count(new LambdaQueryWrapper<ProductComment>()
                .eq(ProductComment::getProductId, productId).eq(ProductComment::getIsDelete, 0)
                .eq(ProductComment::getStatus, 1));
    }

    /**
     * 订单详情，显示2条评论
     * @param productId
     * @return
     */
    public List<CommentVo> getListForDetail(Integer productId){
        List<ProductComment> list = this.list(new LambdaQueryWrapper<ProductComment>()
                .eq(ProductComment::getProductId, productId).eq(ProductComment::getIsDelete, 0)
                .eq(ProductComment::getStatus, 1).last("limit 2"));
        return list.stream().map(e -> {
            return this.transVo(e);
        }).collect(Collectors.toList());
    }

    /**
     * vo转换
     * @param item
     * @return
     */
    private CommentVo transVo(ProductComment item){
        CommentVo vo = new CommentVo();
        BeanUtils.copyProperties(item, vo);
        User user = userService.getById(vo.getUserId());
        vo.setAvatarUrl(user.getAvatarurl());
        vo.setNickname(user.getNickname());
        if (item.getIsPicture() == 1) {
            vo.setImage(productCommentImageService.list(new LambdaQueryWrapper<ProductCommentImage>()
                            .eq(ProductCommentImage::getCommentId, item.getCommentId())
                            .orderByAsc(ProductCommentImage::getId))
                    .stream().map(e -> {
                        return uploadFileUtils.getFilePath(e.getImageId());
                    }).collect(Collectors.toList()));
        }
        return vo;
    }
}
