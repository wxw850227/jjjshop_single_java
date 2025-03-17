package net.jjjshop.shop.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.settings.Express;
import net.jjjshop.common.vo.order.OrderProductVo;
import net.jjjshop.shop.vo.store.ExtractStoreVo;
import net.jjjshop.shop.vo.store.StoreClerkVo;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("订单详情VO")
public class OrderVo extends Order {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("购买用户")
    private String nickname;

    @ApiModelProperty("用户手机号")
    private String mobile;

    @ApiModelProperty("订单来源")
    private String orderSourceText;

    @ApiModelProperty("订单状态")
    private String orderStatusText;

    @ApiModelProperty("支付方式")
    private String payTypeText;

    @ApiModelProperty("配送方式")
    private String deliveryTypeText;

    @ApiModelProperty("支付状态文本")
    private String payStatusText;

    @ApiModelProperty("发货状态文本")
    private String deliveryStatusText;

    @ApiModelProperty("收货状态(10未收货 20已收货)文本")
    private String receiptStatusText;

    @ApiModelProperty("购买商品")
    private List<OrderProductVo> productList;

    @ApiModelProperty("地址详情")
    private OrderAddressVo address;

    @ApiModelProperty("所有物流公司")
    private List<Express> expressList;

    @ApiModelProperty("发货物流公司")
    private Express express;

    @ApiModelProperty("后台更新价格类型")
    private String updatePriceSymbol;

    @ApiModelProperty("核销门店")
    private ExtractStoreVo extractStore;

    @ApiModelProperty("店员列表")
    private List<StoreClerkVo> shopClerkList;

    @ApiModelProperty("核销员工姓名")
    private String extractClerkName;

    @ApiModelProperty("核销门店")
    private String extractStoreName;


}
