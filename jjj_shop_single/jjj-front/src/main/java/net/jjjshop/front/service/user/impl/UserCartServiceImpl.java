package net.jjjshop.front.service.user.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.entity.user.UserCart;
import net.jjjshop.common.entity.user.UserGrade;
import net.jjjshop.common.service.user.UserGradeService;
import net.jjjshop.common.util.ProductUtils;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.common.vo.product.ProductSkuVo;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.front.mapper.user.UserCartMapper;
import net.jjjshop.front.service.product.ProductService;
import net.jjjshop.front.service.user.UserCartService;
import net.jjjshop.front.vo.product.ProductCartVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 用户购物车 服务实现类
 * @author jjjshop
 * @since 2022-08-02
 */
@Slf4j
@Service
public class UserCartServiceImpl extends BaseServiceImpl<UserCartMapper, UserCart> implements UserCartService {
    @Autowired
    private UserCartMapper userCartMapper;
    @Autowired
    private UploadFileUtils uploadFileUtils;
    @Autowired
    private ProductUtils productUtils;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserGradeService userGradeService;

    /**
     * 获取购物车所有商品
     * @param user
     * @return
     */
    public List<ProductCartVo> getAll(User user) {
        List<ProductCartVo> voList = userCartMapper.getAll(user.getUserId());
        for(int i=voList.size()-1;i>=0;i--){
            ProductCartVo item = voList.get(i);
            // 如果商品不存在或者已删除或下架、或者sku已删除/则删除购物车
            if(item.getSpecType() == null || item.getIsDelete() == 1 || item.getProductStatus() != 10
                    || item.getStockNum() == null){
                voList.remove(i);
                this.removeById(item.getCartId());
            }
            item.setProductImage(uploadFileUtils.getImagePathByProductId(item.getProductId()));
            // 多规格文字内容
            item.setProductAttr(productUtils.getProductAttr(item.getProductId(), item.getSpecSkuId(), item.getSpecType()));
            this.setProductListGradeMoney(item, user);
        }
        return voList;
    }

    /**
     * 设置商品的会员价
     * @param vo
     * @param user
     */
    public void setProductListGradeMoney(ProductCartVo vo, User user) {
        vo.setIsUserGrade(false);
        if (user == null || user.getGradeId() == 0) {
            return;
        }
        UserGrade grade = userGradeService.getById(user.getGradeId());
        if (grade == null || grade.getIsDelete() == 1) {
            return;
        }
        // 判断商品是否参与会员折扣
        if (vo.getIsEnableGrade() == 0) {
            return;
        }
        BigDecimal discountRatio = null;
        int aloneGradeType = 10;
        // 商品单独设置了会员折扣
        JSONObject aloneGradeEquity = JSON.parseObject(vo.getAloneGradeEquity());
        if (vo.getIsAloneGrade() == 1 &&
                StringUtils.isNotEmpty(aloneGradeEquity.getString("" + user.getGradeId()))) {
            if (vo.getAloneGradeType() == 10) {
                // 折扣比例
                discountRatio = BigDecimal.valueOf(aloneGradeEquity.getDoubleValue("" + user.getGradeId())).divide(new BigDecimal(100));
            } else {
                aloneGradeType = 20;
                discountRatio = BigDecimal.valueOf(aloneGradeEquity.getDoubleValue("" + user.getGradeId())).divide(vo.getProductPrice(),2, BigDecimal.ROUND_DOWN);
            }
        } else {
            // 折扣比例
            discountRatio = new BigDecimal(grade.getEquity()).divide(new BigDecimal(100));
        }
        if (discountRatio.compareTo(BigDecimal.ONE) < 0) {
            // 标记参与会员折扣
            vo.setIsUserGrade(true);
            // 会员折扣后的商品总金额
            if (aloneGradeType == 20) {
                vo.setProductPrice(aloneGradeEquity.getBigDecimal("" + user.getGradeId()));
            } else {
                vo.setProductPrice(vo.getProductPrice().multiply(discountRatio));
            }
        } else {
            vo.setIsUserGrade(false);
        }
    }

    /**
     * 添加商品
     * @param user
     * @param productId
     * @param specSkuId
     * @param totalNum
     * @return
     */
    public Boolean add(User user, Integer productId, String specSkuId, Integer totalNum) {
        Product product = productService.getById(productId);
        // 商品sku信息
        ProductSkuVo productSku = productUtils.getProductSku(product.getProductId(), product.getSpecType(), specSkuId);
        // 验证商品能否加入
        this.checkProduct(productSku, product, totalNum);
        UserCart cart = this.detail(user.getUserId(), productId, specSkuId);
        if (cart != null) {
            // 更新
            UserCart updateBean = new UserCart();
            updateBean.setCartId(cart.getCartId());
            updateBean.setTotalNum(cart.getTotalNum() + totalNum);
            return this.updateById(updateBean);
        } else {
            // 新增
            UserCart saveBean = new UserCart();
            saveBean.setJoinPrice(productSku.getProductPrice());
            saveBean.setUserId(user.getUserId());
            saveBean.setSpecSkuId(specSkuId);
            saveBean.setTotalNum(totalNum);
            saveBean.setProductId(productId);
            return this.save(saveBean);
        }
    }

    /**
     * 验证商品是否可以购买
     * @param productSku
     * @param product
     * @param totalNum
     * @return
     */
    private void checkProduct(ProductSkuVo productSku, Product product, Integer totalNum) {
        // 判断商品是否下架
        if (product == null
                || product.getIsDelete().intValue() == 1
                || product.getProductStatus().intValue() != 10) {
            throw new BusinessException("很抱歉，商品信息不存在或已下架");
        }
        // 判断商品库存
        if (totalNum > productSku.getStockNum()) {
            throw new BusinessException("很抱歉，商品库存不足");
        }
    }


    /**
     * 详情
     * @param userId
     * @param productId
     * @param specSkuId
     * @return
     */
    public UserCart detail(Integer userId, Integer productId, String specSkuId) {
        return this.getOne(new LambdaQueryWrapper<UserCart>()
                .eq(UserCart::getUserId, userId)
                .eq(UserCart::getProductId, productId)
                .eq(UserCart::getSpecSkuId, specSkuId));
    }

    /**
     * 移除商品
     * @param user
     * @param productId
     * @param specSkuId
     * @return
     */
    public Boolean sub(User user, Integer productId, String specSkuId) {
        UserCart cart = this.detail(user.getUserId(), productId, specSkuId);
        if (cart.getTotalNum().intValue() > 1) {
            // 更新
            UserCart updateBean = new UserCart();
            updateBean.setCartId(cart.getCartId());
            updateBean.setTotalNum(cart.getTotalNum() - 1);
            return this.updateById(updateBean);
        } else {
            // 删除
            return this.removeById(cart.getCartId());
        }
    }

    /**
     * 删除商品
     * @param user
     * @param cartIds
     * @return
     */
    public Boolean delete(User user, Integer[] cartIds) {
        return this.remove(new LambdaQueryWrapper<UserCart>().eq(UserCart::getUserId, user.getUserId())
                .in(UserCart::getCartId, cartIds));
    }

    /**
     * 购物车商品数量
     * @param user
     * @return
     */
    public Integer getTotalCartNum(User user) {
        if(user == null){
            return 0;
        }
        QueryWrapper<UserCart> wrapper = new QueryWrapper<>();
        wrapper.select("sum(total_num) as totalNum");
        wrapper.eq("user_id", user.getUserId());
        UserCart cart = this.getOne(wrapper);
        if (cart == null) {
            return 0;
        } else {
            return cart.getTotalNum();
        }
    }

    /**
     * 下单后，购物车移除
     * @param cartIds
     */
    public void clearAll(String cartIds){
        String[] cartIdList = cartIds.split(",");
        for (String cartId : cartIdList) {
            int index = cartId.indexOf("_");
            Integer productId = Integer.valueOf(cartId.substring(0, index));
            String specSkuId = cartId.substring(index + 1);
            this.remove(new LambdaQueryWrapper<UserCart>()
                    .eq(UserCart::getProductId, productId).eq(UserCart::getSpecSkuId, specSkuId));
        }
    }
}
