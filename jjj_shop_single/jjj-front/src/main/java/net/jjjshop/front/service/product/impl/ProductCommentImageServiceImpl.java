package net.jjjshop.front.service.product.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.ProductCommentImage;
import net.jjjshop.common.mapper.product.ProductCommentImageMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.front.service.product.ProductCommentImageService;
import org.springframework.stereotype.Service;

/**
 * 订单评价图片记录表 服务实现类
 * @author jjjshop
 * @since 2022-06-28
 */
@Slf4j
@Service
public class ProductCommentImageServiceImpl extends BaseServiceImpl<ProductCommentImageMapper, ProductCommentImage> implements ProductCommentImageService {
}
