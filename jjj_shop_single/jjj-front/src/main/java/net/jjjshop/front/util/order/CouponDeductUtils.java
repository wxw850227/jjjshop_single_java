package net.jjjshop.front.util.order;

import net.jjjshop.front.util.order.vo.TempProductBuyVo;
import net.jjjshop.front.vo.product.ProductBuyVo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 优惠券抵扣类
 */
public class CouponDeductUtils {

    public static BigDecimal setProductCouponMoney(List<ProductBuyVo> productList, BigDecimal reducedMoney)
    {
        BigDecimal actualReducedMoney = BigDecimal.ZERO;
        List<TempProductBuyVo> tempProductList = new ArrayList<>();
        // 统计订单商品总金额,(单位分)
        BigDecimal orderTotalPrice = BigDecimal.ZERO;
        for (ProductBuyVo product:productList) {
            TempProductBuyVo tempVo = new TempProductBuyVo();
            tempVo.setProductId(product.getProductId());
            tempVo.setTotalPrice(product.getTotalPrice());
            tempVo.setTotalPriceFen(product.getTotalPrice().multiply(new BigDecimal(100)));
            tempProductList.add(tempVo);
            orderTotalPrice = orderTotalPrice.add(tempVo.getTotalPriceFen());
        }
        // 计算实际抵扣金额
        reducedMoney = reducedMoney.multiply(new BigDecimal(100));
        if(reducedMoney.compareTo(orderTotalPrice) >= 0){
            actualReducedMoney = orderTotalPrice;
        }else{
            actualReducedMoney = reducedMoney;
        }
        // 实际抵扣金额为0，
        if (actualReducedMoney.compareTo(BigDecimal.ZERO) > 0) {
            // 计算商品的价格权重
            // 除去最后一个的权重
            BigDecimal totalWeight = BigDecimal.ZERO;
            for (int i=0;i<tempProductList.size();i++) {
                if(i == tempProductList.size() - 1){
                    // 重新设置最后1个权重
                    tempProductList.get(i).setWeight(BigDecimal.ONE.subtract(totalWeight).setScale(2, RoundingMode.DOWN));
                }else{
                    tempProductList.get(i).setWeight(tempProductList.get(i).getTotalPriceFen().divide(orderTotalPrice, RoundingMode.DOWN).setScale(2, RoundingMode.DOWN));
                    totalWeight = totalWeight.add(tempProductList.get(i).getWeight()).setScale(2, RoundingMode.DOWN);
                }
            }
            // 按权重降序排序
            tempProductList = tempProductList.stream().
                    sorted(Comparator.comparing(TempProductBuyVo::getWeight, Comparator.reverseOrder())).
                    collect(Collectors.toList());
            // 计算商品优惠券抵扣金额
            for (TempProductBuyVo product:tempProductList) {
                product.setCouponMoney(actualReducedMoney.multiply(product.getWeight()).setScale(2, RoundingMode.DOWN));
            }
            // 总抵扣金额
            //BigDecimal totalCouponMoney = tempProductList.stream().map(TempProductBuyVo::getCouponMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
            //setProductListCouponMoneyFill(tempProductList, totalCouponMoney, actualReducedMoney);
            //setProductListCouponMoneyDiff(tempProductList, totalCouponMoney, actualReducedMoney);
        }
        // 分配订单商品优惠券抵扣金额
        BigDecimal totalMoney = BigDecimal.ZERO;
        for (int i=0;i<productList.size();i++) {
            if(i == productList.size() - 1){
                // 重新设置最后1个
                productList.get(i).setCouponMoney(actualReducedMoney.divide(new BigDecimal(100)).subtract(totalMoney).setScale(2, RoundingMode.DOWN));
            }else{
                BigDecimal couponMoney = getTempVo(tempProductList, productList.get(i).getProductId()).getCouponMoney();
                productList.get(i).setCouponMoney(couponMoney.divide(new BigDecimal(100), RoundingMode.DOWN).setScale(2, RoundingMode.DOWN));
                totalMoney = totalMoney.add(productList.get(i).getCouponMoney());
            }
        }
        return actualReducedMoney;
    }

    private static TempProductBuyVo getTempVo(List<TempProductBuyVo> tempProductList, Integer productId){
        for (TempProductBuyVo product:tempProductList) {
            if(productId.intValue() == product.getProductId().intValue()){
                return product;
            }
        }
        return null;
    }

    private static void setProductListCouponMoneyFill(List<TempProductBuyVo> tempProductList, BigDecimal totalCouponMoney, BigDecimal actualReducedMoney) {
        if (totalCouponMoney.compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal temReducedMoney = actualReducedMoney;
            for (TempProductBuyVo product:tempProductList) {
                if (temReducedMoney.compareTo(BigDecimal.ZERO) == 0) break;
                product.setCouponMoney(BigDecimal.ONE);
                temReducedMoney = temReducedMoney.subtract(BigDecimal.ONE);
            }
        }
    }

    private static void setProductListCouponMoneyDiff(List<TempProductBuyVo> tempProductList, BigDecimal totalCouponMoney, BigDecimal actualReducedMoney)
    {
        BigDecimal tempDiff = actualReducedMoney.subtract(totalCouponMoney);
        for (TempProductBuyVo product:tempProductList) {
        if (tempDiff.compareTo(BigDecimal.ONE) < 0) break;
            product.setCouponMoney(product.getCouponMoney().add(BigDecimal.ONE));
            tempDiff = tempDiff.subtract(BigDecimal.ONE);
        }
    }
}
