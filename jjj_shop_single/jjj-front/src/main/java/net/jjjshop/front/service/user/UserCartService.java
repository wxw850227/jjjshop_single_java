package net.jjjshop.front.service.user;

import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.entity.user.UserCart;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.front.vo.product.ProductCartVo;

import java.util.List;

/**
 * 用户购物车 服务类
 * @author jjjshop
 * @since 2022-08-02
 */
public interface UserCartService extends BaseService<UserCart> {

    /**
     * 获取购物车所有商品
     * @param user
     * @return
     */
    List<ProductCartVo> getAll(User user);

    /**
     * 添加商品
     * @param user
     * @param productId
     * @param specSkuId
     * @param totalNum
     * @return
     */
    Boolean add(User user, Integer productId, String specSkuId, Integer totalNum);

    /**
     * 移除商品
     * @param user
     * @param productId
     * @param specSkuId
     * @return
     */
    Boolean sub(User user, Integer productId, String specSkuId);

    /**
     * 删除商品
     * @param user
     * @param cartIds
     * @return
     */
    Boolean delete(User user, Integer[] cartIds);

    /**
     * 购物车商品数量
     * @param user
     * @return
     */
    Integer getTotalCartNum(User user);

    /**
     * 详情
     * @param userId
     * @param productId
     * @param specSkuId
     * @return
     */
    UserCart detail(Integer userId, Integer productId, String specSkuId);

    /**
     * 下单后，购物车移除
     * @param cartIds
     */
    void clearAll(String cartIds);
}
