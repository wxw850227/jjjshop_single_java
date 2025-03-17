package net.jjjshop.front.util.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.front.vo.store.StoreVo;
import net.jjjshop.front.vo.user.UserAddressVo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
@ApiModel(value = "订单信息", description = "订单信息")
public class OrderData implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("配送类型")
    private Integer delivery;

    @ApiModelProperty("配送地址")
    private UserAddressVo address;

    @ApiModelProperty("是否存在地址")
    private Boolean existAddress;

    @ApiModelProperty("配送费用")
    private BigDecimal expressPrice;

    @ApiModelProperty("当前用户收货城市是否存在配送规则中")
    private Boolean intraRegion;

    @ApiModelProperty("配送地址")
    private StoreVo extractStore;

    @ApiModelProperty("记忆的自提联系方式")
    private Map<String, String> lastExtract;

    @ApiModelProperty("系统支持的配送方式")
    private List<Integer> deliverySetting;

    @ApiModelProperty("订单金额(含优惠折扣)")
    private BigDecimal orderPrice;

    @ApiModelProperty("订单实付款金额(订单金额 + 运费)")
    private BigDecimal orderPayPrice;

    @ApiModelProperty("订单的商品总金额(不含优惠折扣)")
    private BigDecimal orderTotalPrice;

    @ApiModelProperty("使用优惠券id")
    private Integer couponId;

    @ApiModelProperty("优惠券抵扣金额")
    private BigDecimal couponMoney;
}
