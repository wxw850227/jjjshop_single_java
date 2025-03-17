package net.jjjshop.common.entity.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 售后单记录表
 *
 * @author jjjshop
 * @since 2022-07-04
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_order_refund")
@ApiModel(value = "OrderRefund对象")
public class OrderRefund implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("售后单id")
    @TableId(value = "order_refund_id", type = IdType.AUTO)
    private Integer orderRefundId;

    @ApiModelProperty("订单id")
    private Integer orderId;

    @ApiModelProperty("订单商品id")
    private Integer orderProductId;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("售后类型(10退货退款 20换货 30退款)")
    private Integer type;

    @ApiModelProperty("用户申请原因(说明)")
    private String applyDesc;

    @ApiModelProperty("商家审核状态(0待审核 10已同意 20已拒绝)")
    private Integer isAgree;

    @ApiModelProperty("商家拒绝原因(说明)")
    private String refuseDesc;

    @ApiModelProperty("实际退款金额")
    private BigDecimal refundMoney;

    @ApiModelProperty("用户是否发货(0未发货 1已发货)")
    private Integer isUserSend;

    @ApiModelProperty("用户发货时间")
    private Date sendTime;

    @ApiModelProperty("用户发货物流公司id")
    private String expressId;

    @ApiModelProperty("用户发货物流单号")
    private String expressNo;

    @ApiModelProperty("商家收货状态(0未收货 1已收货)")
    private Integer isReceipt;

    @ApiModelProperty("售后单状态(0进行中 10已拒绝 20已完成 30已取消)")
    private Integer status;

    @ApiModelProperty("平台发货时间")
    private Date deliverTime;

    @ApiModelProperty("平台发货物流公司id")
    private String sendExpressId;

    @ApiModelProperty("平台发货物流单号")
    private String sendExpressNo;

    @ApiModelProperty("平台是否发货(0未发货 1已发货)")
    private Integer isPlateSend;

    @ApiModelProperty("程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
