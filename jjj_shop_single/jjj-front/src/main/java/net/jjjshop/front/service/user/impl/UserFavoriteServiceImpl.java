package net.jjjshop.front.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.entity.user.UserFavorite;
import net.jjjshop.common.mapper.user.UserFavoriteMapper;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.front.param.user.UserFavoritePageParam;
import net.jjjshop.front.service.product.ProductService;
import net.jjjshop.front.service.user.UserFavoriteService;
import net.jjjshop.front.service.user.UserService;
import net.jjjshop.front.vo.user.UserFavoriteVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 我的收藏 服务实现类
 * @author jjjshop
 * @since 2022-08-02
 */

@Slf4j
@Service
public class UserFavoriteServiceImpl extends BaseServiceImpl<UserFavoriteMapper, UserFavorite> implements UserFavoriteService {

    @Autowired
    private ProductService productService;
    @Autowired
    private UploadFileUtils uploadFileUtils;
    @Autowired
    private UserService userService;

    /**
     * 收藏列表
     * @param userFavoritePageParam
     * @return
     */
    public Paging<UserFavoriteVo> getList(UserFavoritePageParam userFavoritePageParam) {
        Integer userId = userFavoritePageParam.getUserId();
        Page page = new PageInfo(userFavoritePageParam);
        IPage<UserFavorite> userFavorite = this.page(page, new LambdaQueryWrapper<UserFavorite>().eq(UserFavorite::getUserId, userId).orderByDesc(UserFavorite::getCreateTime));
        IPage<UserFavoriteVo> result = userFavorite.convert(item -> {
            UserFavoriteVo vo = new UserFavoriteVo();
            BeanUtils.copyProperties(item, vo);
            Product product = productService.getById(vo.getProductId());
            vo.setProductImage(uploadFileUtils.getImagePathByProductId(vo.getProductId()));
            vo.setProductId(product.getProductId());
            vo.setProductName(product.getProductName());
            vo.setProductPrice(product.getProductPrice());
            vo.setLinePrice(product.getLinePrice());
            return vo;
        });
        return new Paging(result);
    }

    /**
     * 添加收藏
     * @param productId
     * @param userId
     * @return
     */
    public Boolean add(Integer productId, Integer userId) {
        UserFavorite favorite = new UserFavorite();
        favorite.setUserId(userId);
        favorite.setProductId(productId);
        favorite.setAppId(userService.getById(userId).getAppId());
        return this.save(favorite);
    }

    /**
     * 取消收藏
     * @param productId
     * @param userId
     * @return
     */
    public Boolean delById(Integer productId, Integer userId){
        return this.remove(new LambdaQueryWrapper<UserFavorite>().eq(UserFavorite::getUserId, userId).eq(UserFavorite::getProductId, productId));
    }

    /**
     * 用户是否收藏
     * @param productId
     * @param userId
     * @return
     */
    public Boolean isFav(Integer productId, Integer userId){
        int count = this.count(new LambdaQueryWrapper<UserFavorite>().eq(UserFavorite::getUserId, userId)
                .eq(UserFavorite::getProductId, productId));
        return count > 0;
    }
}
