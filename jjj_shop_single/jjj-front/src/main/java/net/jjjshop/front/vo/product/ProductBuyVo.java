

package net.jjjshop.front.vo.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.entity.settings.Delivery;
import net.jjjshop.common.entity.settings.DeliveryRule;
import net.jjjshop.common.vo.product.ProductSkuVo;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("购买商品VO")
public class ProductBuyVo extends Product {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("购买商品sku")
    private ProductSkuVo productSku;

    @ApiModelProperty("购买数量")
    private Integer totalNum;

    @ApiModelProperty("购买规格")
    private String specSkuId;

    @ApiModelProperty("商品总价")
    private BigDecimal totalPrice;

    @ApiModelProperty("商品支付金额")
    private BigDecimal totalPayPrice;

    @ApiModelProperty("商品图片")
    private String productImage;

    @ApiModelProperty("运费")
    private BigDecimal expressPrice;

    @ApiModelProperty("运费模板规则")
    private List<DeliveryRule> deliveryRuleList;

    @ApiModelProperty("运费模板")
    private Delivery delivery;

    @ApiModelProperty("优惠券抵扣金额")
    private BigDecimal couponMoney;

    @ApiModelProperty("是否参与会员折扣")
    private Boolean isUserGrade;

    @ApiModelProperty("会员等级抵扣的金额")
    private BigDecimal gradeRatio;

    @ApiModelProperty("会员折扣的商品单价")
    private BigDecimal gradeProductPrice;

    @ApiModelProperty("会员折扣的总额差")
    private BigDecimal gradeTotalMoney;
}
