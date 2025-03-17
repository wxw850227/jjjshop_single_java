

package net.jjjshop.front.param.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 订单参数
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "订单参数对象", description = "订单参数对象")
public class OrderBuyParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("发货方式")
    private Integer delivery;

    @ApiModelProperty("上门自提门店id")
    private Integer storeId;

    @ApiModelProperty("使用优惠券id")
    private Integer couponId;

    @ApiModelProperty("支付渠道来源")
    private String paySource;

    @ApiModelProperty("商品id")
    private Integer productId;

    @ApiModelProperty("购买商品数量")
    private Integer productNum;

    @ApiModelProperty("商品skuId")
    private String specSkuId;

    @ApiModelProperty("自提电话")
    private String phone;

    @ApiModelProperty("自提联系人")
    private String linkman;

    @ApiModelProperty("订单备注")
    private String remark;

    @ApiModelProperty("购物车")
    private String cartIds;
}
