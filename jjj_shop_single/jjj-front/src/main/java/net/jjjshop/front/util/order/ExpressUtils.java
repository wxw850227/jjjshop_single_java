package net.jjjshop.front.util.order;

import net.jjjshop.common.entity.settings.DeliveryRule;
import net.jjjshop.common.settings.vo.TradeVo;
import net.jjjshop.front.vo.product.ProductBuyVo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 运费计算类
 */
public class ExpressUtils {
    /**
     * 根据用户收货城市id 验证是否在商品配送规则中
     */
    public static ProductBuyVo getNotInRuleProduct(List<ProductBuyVo> productList, Integer cityId)
    {
        if (cityId > 0) {
            for (ProductBuyVo product:productList) {
                if(product.getIsVirtual() == 1){
                    continue;
                }
                List<String> cityIds = new ArrayList<>();
                for(DeliveryRule rule:product.getDeliveryRuleList()){
                    String[] regionData = rule.getRegion().split(",");
                    cityIds.addAll(Arrays.asList(regionData));
                }
                if(!cityIds.contains("" + cityId)){
                    return product;
                }
            }

        }
        return null;
    }

    /**
     * 设置订单商品的运费
     */
    public static void setExpressPrice(List<ProductBuyVo> productList, Integer cityId) {
        // 订单商品总金额
        BigDecimal orderTotalPrice = productList.stream().map(ProductBuyVo::getTotalPayPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        for (ProductBuyVo product:productList) {
            // 如果是虚拟物品，则为0
            if (product.getIsVirtual() == 1) {
                product.setExpressPrice(BigDecimal.ZERO);
            } else {
                product.setExpressPrice(onCalcProductFreight(product, cityId));
            }
        }
    }

    /**
     * 计算商品的配送费用
     */
    private static BigDecimal onCalcProductFreight(ProductBuyVo product, Integer cityId){
        // 当前收货城市配送规则
        DeliveryRule rule = getCityDeliveryRule(product, cityId);
        if(rule == null){
            return BigDecimal.ZERO;
        }
        // 商品总重量
        double totalWeight = product.getProductSku().getProductWeight() * product.getTotalNum();
        // 商品总数量or总重量
        double total = product.getDelivery().getMethod() == 10 ? product.getTotalNum() : totalWeight;
        if (total <= rule.getFirst()) {
            return rule.getFirstFee();
        }
        // 续件or续重 数量
        double additional = total - rule.getFirst();
        if (additional <= rule.getAdditional()) {
            return rule.getFirstFee().add(rule.getAdditionalFee());
        }
        // 计算续重/件金额
        BigDecimal additionalFee = BigDecimal.ZERO;
        if (rule.getAdditional() < 1) {
            // 配送规则中续件为0
            return additionalFee;
        } else {
            additionalFee = rule.getAdditionalFee().divide(new BigDecimal(rule.getAdditional())).multiply(new BigDecimal(additional));
        }
        return rule.getFirstFee().add(additionalFee);
    }

    /**
     * 根据城市id获取规则信息
     */
    private static DeliveryRule getCityDeliveryRule(ProductBuyVo product, Integer cityId){
        for(DeliveryRule rule:product.getDeliveryRuleList()) {
            String[] regionData = rule.getRegion().split(",");
            if (Arrays.asList(regionData).contains("" + cityId)) {
                return rule;
            }
        }
        return null;
    }

    /**
     * 获取订单最终运费
     */
    public static BigDecimal getTotalFreight(List<ProductBuyVo> productList, TradeVo tradeVo)
    {
        // 所有商品的运费金额
        List<BigDecimal> expressPriceArr = new ArrayList<>();
        for (ProductBuyVo product:productList) {
            expressPriceArr.add(product.getExpressPrice());
        }
        // 计算最终运费
        BigDecimal expressPrice = BigDecimal.ZERO;
        switch (tradeVo.getFreightRule()) {
            case 10:    // 策略1: 叠加
                expressPrice = expressPriceArr.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
                break;
            case 20:    // 策略2: 以最低运费结算
                expressPrice = expressPriceArr.stream().min((x1, x2) -> x1.compareTo(x2)).get();
                break;
            case 30:    // 策略3: 以最高运费结算
                expressPrice = expressPriceArr.stream().max((x1, x2) -> x1.compareTo(x2)).get();
                break;
        }
        return expressPrice;
    }
}
