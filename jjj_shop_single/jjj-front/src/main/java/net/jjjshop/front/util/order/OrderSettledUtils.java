package net.jjjshop.front.util.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.ProductCategory;
import net.jjjshop.common.entity.settings.DeliveryRule;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.entity.user.UserGrade;
import net.jjjshop.common.enums.DeliveryTypeEnum;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.service.user.UserGradeService;
import net.jjjshop.common.settings.vo.StoreVo;
import net.jjjshop.common.settings.vo.TradeVo;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.front.param.order.OrderBuyParam;
import net.jjjshop.front.param.order.OrderCreateParam;
import net.jjjshop.front.service.order.OrderService;
import net.jjjshop.front.service.product.ProductCategoryService;
import net.jjjshop.front.service.settings.DeliveryRuleService;
import net.jjjshop.front.service.settings.DeliveryService;
import net.jjjshop.front.service.store.StoreService;
import net.jjjshop.front.service.user.UserAddressService;
import net.jjjshop.front.service.user.UserCouponService;
import net.jjjshop.front.util.order.vo.OrderData;
import net.jjjshop.front.util.order.vo.OrderSource;
import net.jjjshop.front.util.order.vo.SettledRule;
import net.jjjshop.front.vo.coupon.UserCouponVo;
import net.jjjshop.front.vo.product.ProductBuyVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 订单结算父类
 */
@Slf4j
@Component
@Scope("prototype")
public class OrderSettledUtils {
    @Autowired
    private OrderService orderService;
    @Autowired
    private SettingUtils settingUtils;
    @Autowired
    private UserAddressService userAddressService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private DeliveryRuleService deliveryRuleService;
    @Autowired
    private DeliveryService deliveryService;
    @Autowired
    private UserCouponService userCouponService;
    @Autowired
    private UserGradeService userGradeService;
    @Autowired
    private ProductCategoryService productCategoryService;

    // 用户信息
    protected User user;
    // 购买商品信息
    protected List<ProductBuyVo> productList;
    // 前端参数
    protected OrderBuyParam orderBuyParam;
    // 订单来源
    protected OrderSource orderSource = new OrderSource();
    // 结算规则
    protected SettledRule settledRule = new SettledRule();
    // 订单信息
    protected OrderData orderData = new OrderData();

    public Map<String, Object> settlement(User user, List<ProductBuyVo> productList, OrderBuyParam orderBuyParam){
        this.user = user;
        this.productList = productList;
        this.orderBuyParam = orderBuyParam;
        Map<String, Object> result = new HashMap<>();
        // 订单信息，初始化
        this.getOrderData();
        // 验证商品状态, 是否允许购买
        this.validateProductList();
        // 订单商品总数量
        Integer orderTotalNum = this.productList.stream().mapToInt(ProductBuyVo::getTotalNum).sum();
        // 设置订单商品会员折扣价
        this.setOrderGrade();
        // 设置订单商品总金额(不含优惠折扣)
        this.setOrderTotalPrice();
        // 当前用户可用的优惠券列表
        List<UserCouponVo> couponList = this.getUserCouponList(this.orderData.getOrderTotalPrice());
        for (int i = couponList.size() - 1; i >= 0; i--) {
            if(!this.checkCouponCanUse(couponList.get(i))){
                couponList.remove(i);
            }
        }
        // 计算优惠券抵扣
        this.setOrderCouponMoney(couponList, this.orderBuyParam.getCouponId());
        // 计算订单商品的实际付款金额
        this.setOrderProductPayPrice();
        // 处理配送方式
        if (this.orderData.getDelivery().intValue() == DeliveryTypeEnum.EXPRESS.getValue().intValue()) {
            this.setOrderExpress();
        } else if (this.orderData.getDelivery().intValue() == DeliveryTypeEnum.EXTRACT.getValue().intValue()) {
            if(this.orderBuyParam.getStoreId() > 0){
                this.orderData.setExtractStore(storeService.getStoreVoById(this.orderBuyParam.getStoreId()));
            }
        }

        // 计算订单最终金额
        this.setOrderPayPrice();
        result.put("orderTotalNum", orderTotalNum);
        result.put("orderData", this.orderData);
        result.put("couponList", couponList);
        result.put("settledRule", this.settledRule);
        return result;
    }

    /**
     * 订单基本信息
     */
    private void getOrderData(){
        this.orderData = new OrderData();
        // 配送方式
        JSONObject vo = settingUtils.getSetting(SettingEnum.STORE.getKey(), null);
        StoreVo storeVo = JSONObject.parseObject(vo.toJSONString(), StoreVo.class);
        Integer delivery = 0;
        if (this.productList.get(0).getIsVirtual() == 1) {
            delivery = 30;
        } else {
            delivery = this.orderBuyParam.getDelivery()  > 0 ? this.orderBuyParam.getDelivery() : storeVo.getDeliveryType().get(0);
        }
        this.orderData.setDelivery(delivery);
        this.orderData.setDeliverySetting(storeVo.getDeliveryType());
        // 默认地址。
        this.orderData.setExistAddress(this.user.getAddressId() > 0?true:false);
        if(this.user.getAddressId() > 0){
            this.orderData.setAddress(userAddressService.detail(this.user.getAddressId()));
        }
        // 配送费用
        this.orderData.setExpressPrice(BigDecimal.ZERO);
        // 当前用户收货城市是否存在配送规则中
        this.orderData.setIntraRegion(true);
        // 自提门店信息
        this.orderData.setExtractStore(null);
        // 记忆的自提联系方式
        this.orderData.setLastExtract(this.getLastExtract(this.user.getUserId()));
    }

    /**
     * 设置订单的商品总金额(不含优惠折扣)
     */
    private void setOrderTotalPrice()
    {
        // 订单商品的总金额(不含优惠券折扣)
        this.orderData.setOrderTotalPrice(this.productList.stream().map(ProductBuyVo::getTotalPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    /**
     * 计算订单商品的实际付款金额
     */
    private void setOrderProductPayPrice()
    {
        // 商品总价 - 优惠抵扣
        for(ProductBuyVo product:this.productList){
            BigDecimal value = product.getTotalPrice();
            // 减去优惠券抵扣金额
            value = value.subtract(product.getCouponMoney());
            product.setTotalPayPrice(value);
        }
    }

    /**
     * 设置最终支付金额
     */
    private void setOrderPayPrice(){
        // 订单金额(含优惠折扣)
        this.orderData.setOrderPrice(this.productList.stream().map(ProductBuyVo::getTotalPayPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
        // 订单实付款金额(订单金额 + 运费)
        this.orderData.setOrderPayPrice(this.orderData.getOrderPrice().add(this.orderData.getExpressPrice()));
    }

    /**
     * 验证商品是否可以购买，子类实现
     */
    protected void validateProductList(){

    }

    /**
     * 创建新订单
     */
    public Integer createOrder(User user, List<ProductBuyVo> productList, OrderBuyParam orderBuyParam)
    {
        // 结算数据
        this.settlement(user, productList, orderBuyParam);
        // 表单验证
        this.validateOrderForm(orderBuyParam);
        // 创建订单
        OrderCreateParam params = new OrderCreateParam();
        params.setOrderBuyParam(orderBuyParam);
        params.setOrderData(orderData);
        params.setSettledRule(settledRule);
        params.setOrderSource(orderSource);
        params.setUser(user);
        params.setProductList(productList);
        return orderService.createOrder(params);
    }

    /**
     * 验证参数
     */
    private void validateOrderForm(OrderBuyParam orderBuyParam){
        if(this.orderData.getDelivery().intValue() == DeliveryTypeEnum.EXPRESS.getValue().intValue()){
            // 快递配送
            if(this.orderData.getAddress() == null){
                throw new BusinessException("请先选择收货地址");
            }
        }else if(this.orderData.getDelivery().intValue() == DeliveryTypeEnum.EXTRACT.getValue().intValue()){
            // 上门自提
            if(this.orderData.getExtractStore() == null){
                throw new BusinessException("请先选择自提门店");
            }
            if(StringUtils.isEmpty(orderBuyParam.getLinkman()) || StringUtils.isEmpty(orderBuyParam.getPhone())){
                throw new BusinessException("请填写联系人和电话");
            }
        }
    }

    private Map<String,String> getLastExtract(Integer userId){
        Map<String,String> result = new HashMap<>();
        result.put("linkman", "");
        result.put("phone", "");
        return result;
    }

    /**
     * 计算运费
     */
    private void setOrderExpress(){
        // 设置默认数据：配送费用
        for (ProductBuyVo product:this.productList) {
            product.setExpressPrice(BigDecimal.ZERO);
            // 运费模板
            product.setDelivery(deliveryService.getById(product.getDeliveryId()));
            // 运费规则
            product.setDeliveryRuleList(deliveryRuleService.list(new LambdaQueryWrapper<DeliveryRule>()
                    .eq(DeliveryRule::getDeliveryId, product.getDeliveryId())));
        }
        // 当前用户收货城市id
        Integer cityId = 0;
        if(this.orderData.getAddress() != null){
            cityId = this.orderData.getAddress().getCityId();
        }

        // 获取不支持当前城市配送的商品
        ProductBuyVo notInRuleProduct = ExpressUtils.getNotInRuleProduct(this.productList, cityId);

        // 验证商品是否在配送范围
        this.orderData.setIntraRegion(notInRuleProduct == null);

        if (!this.orderData.getIntraRegion()) {
            throw new BusinessException(String.format("很抱歉，您的收货地址不在商品 [%s] 的配送范围内", notInRuleProduct.getProductName()));
        } else {
            // 计算配送金额
            ExpressUtils.setExpressPrice(this.productList, cityId);
        }

        // 运费设置
        JSONObject vo = settingUtils.getSetting(SettingEnum.TRADE.getKey(), null);
        TradeVo tradeVo = JSONObject.parseObject(vo.toJSONString(), TradeVo.class);
        // 订单总运费金额
        this.orderData.setExpressPrice(ExpressUtils.getTotalFreight(this.productList, tradeVo));
    }

    /**
     * 当前用户可用的优惠券列表
     */
    private List<UserCouponVo> getUserCouponList(BigDecimal orderTotalPrice)
    {
        // 是否开启优惠券折扣
        if (!this.settledRule.getIsCoupon()) {
            return new ArrayList<>();
        }
        return userCouponService.getUserCouponList(this.user.getUserId(), orderTotalPrice);
    }

    /**
     * 设置订单优惠券抵扣信息
     */
    private void setOrderCouponMoney(List<UserCouponVo> couponList, Integer couponId){
        // 设置默认数据：订单信息
        this.orderData.setCouponId(0);
        this.orderData.setCouponMoney(BigDecimal.ZERO);
        // 设置默认数据：订单商品列表
        for (ProductBuyVo product:this.productList) {
            product.setCouponMoney(BigDecimal.ZERO);
        }
        // 是否开启优惠券折扣
        if (!this.settledRule.getIsCoupon()) {
            return;
        }
        // 如果没有可用的优惠券，直接返回
        if (couponId <= 0 || couponList.size() == 0) {
            return;
        }
        // 获取优惠券信息
        UserCouponVo couponInfo = null;
        for(UserCouponVo coupon:couponList){
            if(coupon.getUserCouponId().intValue() == couponId.intValue()){
                couponInfo = coupon;
                break;
            }
        }
        if (couponInfo == null) {
            throw new BusinessException("'未找到优惠券信息'");
        }
        // 计算订单商品优惠券抵扣金额
        BigDecimal actualReducedMoney = CouponDeductUtils.setProductCouponMoney(this.productList, couponInfo.getReducedPrice());
        // 记录订单优惠券信息
        this.orderData.setCouponId(couponId);
        this.orderData.setCouponMoney(actualReducedMoney.divide(new BigDecimal(100), RoundingMode.DOWN).setScale(2, RoundingMode.DOWN));
    }

    /**
     * 检查优惠券是否可以使用
     */
    private boolean checkCouponCanUse(UserCouponVo coupon)
    {
        // 0无限制
        if (coupon.getFreeLimit() == 1) {

        } else if (coupon.getFreeLimit() == 2) {
            //不可与等级优惠同时
            for (ProductBuyVo product:this.productList) {
                if (product.getIsUserGrade()) {
                    return false;
                }
            }
        } else if (coupon.getFreeLimit() == 3) {
            for (ProductBuyVo product:this.productList) {
                if (product.getIsUserGrade()) {
                    return false;
                }
            }
        }
        // 是否限制商品使用
        if (coupon.getApplyRange() == 20) {
            List<String> productIds = Arrays.asList(coupon.getProductIds().split(","));
            for (ProductBuyVo product:this.productList) {
                if(!productIds.contains("" + product.getProductId())){
                    return false;
                }
            }
        }
        // 是否限制分类使用
        if (coupon.getApplyRange() == 30) {
            JSONObject categoryIds = JSON.parseObject(coupon.getCategoryIds());
            for (ProductBuyVo product:this.productList) {
                // 如果二级分类包含
                if(categoryIds.getJSONArray("second").contains(product.getCategoryId())){
                    continue;
                }
                // 如果一级分类包含
                if(categoryIds.getJSONArray("first").contains(product.getCategoryId())){
                    continue;
                }
                // 如果分类有父类，则看一级分类是否包含
                ProductCategory category = productCategoryService.getById(product.getCategoryId());
                if (category.getParentId() > 0) {
                    if(categoryIds.getJSONArray("second").contains(product.getCategoryId())){
                        continue;
                    }
                }
                return false;
            }
        }
        return true;
    }

    /**
     * 设置订单商品会员折扣价
     */
    private void setOrderGrade()
    {
        // 设置默认数据
        for (ProductBuyVo product:this.productList) {
            product.setIsUserGrade(false);
            product.setGradeRatio(BigDecimal.ZERO);
            product.setGradeProductPrice(BigDecimal.ZERO);
            product.setGradeTotalMoney(BigDecimal.ZERO);
        }
        // 是否开启会员等级折扣
        if(!this.settledRule.getIsUserGrade()){
            return;
        }
        // 计算抵扣金额
        for (ProductBuyVo product:this.productList) {
            // 判断商品是否参与会员折扣
            if(product.getIsEnableGrade() == 0){
                continue;
            }
            BigDecimal discountRatio = null;
            int aloneGradeType = 10;
            // 商品单独设置了会员折扣
            JSONObject aloneGradeEquity = JSON.parseObject(product.getAloneGradeEquity());
            if(product.getIsAloneGrade() == 1 && StringUtils.isNotEmpty(aloneGradeEquity.getString("" +user.getGradeId()))){
                if(product.getAloneGradeType() == 10){
                    // 折扣比例
                    discountRatio = BigDecimal.valueOf(aloneGradeEquity.getDoubleValue("" + user.getGradeId()) * 0.01).setScale(2, RoundingMode.DOWN);
                }else{
                    aloneGradeType = 20;
                    discountRatio = BigDecimal.valueOf(aloneGradeEquity.getDoubleValue("" + user.getGradeId()) * 0.01).setScale(2, RoundingMode.DOWN);
                }
            }else{
                UserGrade grade = userGradeService.getById(user.getGradeId());
                // 折扣比例
                discountRatio = BigDecimal.valueOf(grade.getEquity() * 0.01);
            }
            BigDecimal gradeTotalPrice = null;
            BigDecimal gradeProductPrice = null;
            if (discountRatio.compareTo(BigDecimal.ONE) < 0) {
                // 会员折扣后的商品总金额
                if (aloneGradeType == 20) {
                    gradeTotalPrice = aloneGradeEquity.getBigDecimal("" + user.getGradeId()).multiply(new BigDecimal(product.getTotalNum()));
                    gradeProductPrice = aloneGradeEquity.getBigDecimal("" + user.getGradeId());
                } else {
                    gradeTotalPrice = product.getTotalPrice().multiply(discountRatio);
                    if(gradeTotalPrice.compareTo(BigDecimal.ZERO) < 0){
                        gradeTotalPrice = BigDecimal.ZERO;
                    }
                    gradeProductPrice = product.getTotalPrice().multiply(discountRatio);
                }
                product.setIsUserGrade(true);
                product.setGradeRatio(discountRatio);
                product.setGradeProductPrice(gradeProductPrice);
                product.setGradeTotalMoney(product.getTotalPrice().subtract(gradeTotalPrice));
                product.setTotalPrice(gradeTotalPrice);
            }
        }
    }
}
