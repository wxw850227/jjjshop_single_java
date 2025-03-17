package net.jjjshop.front.service.user;

import net.jjjshop.common.entity.user.UserFavorite;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.front.param.user.UserFavoritePageParam;
import net.jjjshop.front.vo.user.UserFavoriteVo;

/**
 * 我的收藏 服务类
 * @author jjjshop
 * @since 2022-08-02
 */

public interface UserFavoriteService extends BaseService<UserFavorite> {

    /**
     * 收藏列表
     * @param userFavoritePageParam
     * @return
     */
    Paging<UserFavoriteVo> getList(UserFavoritePageParam userFavoritePageParam);

    /**
     * 添加收藏
     * @param productId
     * @param userId
     * @return
     */
    Boolean add(Integer productId, Integer userId);

    /**
     * 取消收藏
     * @param productId
     * @param userId
     * @return
     */
    Boolean delById(Integer productId, Integer userId);

    /**
     * 用户是否收藏
     * @param productId
     * @param userId
     * @return
     */
    Boolean isFav(Integer productId, Integer userId);
}
