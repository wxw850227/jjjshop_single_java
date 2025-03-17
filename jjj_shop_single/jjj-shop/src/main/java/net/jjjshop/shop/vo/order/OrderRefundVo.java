package net.jjjshop.shop.vo.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.order.OrderRefund;
import net.jjjshop.common.entity.order.OrderRefundAddress;
import net.jjjshop.common.entity.settings.Express;
import net.jjjshop.common.entity.settings.ReturnAddress;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.vo.setting.ImageVo;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("售后单VO")
public class OrderRefundVo extends OrderRefund {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品图片")
    private OrderProductVo orderProduct;

    @ApiModelProperty("用户")
    private User user;

    @ApiModelProperty("订单创建时间")
    private Date orderCreateTime;

    @ApiModelProperty("商家处理文本信息")
    private String isAgreeText;

    @ApiModelProperty("售后类型：")
    private String typeText;

    @ApiModelProperty("处理状态：")
    private String statusText;

    @ApiModelProperty("用户退货物流信息")
    private Express express;

    @ApiModelProperty("物流公司信息")
    private List<Express> expressList;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("订单编号")
    private Order order;

    @ApiModelProperty("退货地址列表")
    private List<ReturnAddress> addressList;

    @ApiModelProperty("售后单图片")
    private List<ImageVo> images;

    @ApiModelProperty("退货地址")
    private OrderRefundAddress address;

    @ApiModelProperty("商家发货物流公司")
    private String sendExpressName;


}
