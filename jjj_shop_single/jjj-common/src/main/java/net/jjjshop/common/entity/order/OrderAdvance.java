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
 * 预售定金订单记录表
 *
 * @author jjjshop
 * @since 2022-07-04
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_order_advance")
@ApiModel(value = "OrderAdvance对象")
public class OrderAdvance implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(" 主键id")
    @TableId(value = "order_advance_id", type = IdType.AUTO)
    private Integer orderAdvanceId;

    @ApiModelProperty("预售商品id")
    private Integer advanceProductId;

    @ApiModelProperty("预售商品规格id")
    private Integer advanceProductSkuId;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("预售结束时间")
    private Integer endTime;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("支付定金金额")
    private BigDecimal payPrice;

    @ApiModelProperty("支付方式(10余额支付 20微信支付30支付宝)")
    private Integer payType;

    @ApiModelProperty("余额抵扣金额")
    private BigDecimal balance;

    @ApiModelProperty("在线支付金额")
    private BigDecimal onlineMoney;

    @ApiModelProperty("付款状态(10未付款 20已付款)")
    private Integer payStatus;

    @ApiModelProperty("付款时间")
    private Integer payTime;

    @ApiModelProperty("订单状态10=>进行中，20=>已经取消，30=>已完成")
    private Integer orderStatus;

    @ApiModelProperty("预售订单支付结束时间")
    private Integer payEndTime;

    @ApiModelProperty("主订单id")
    private Integer orderId;

    @ApiModelProperty("支付来源,mp,wx")
    private String paySource;

    @ApiModelProperty("是否允许退款0不允许1允许")
    private Boolean moneyReturn;

    @ApiModelProperty("退款金额")
    private BigDecimal refundMoney;

    @ApiModelProperty("尾款立减金额")
    private BigDecimal reduceMoney;

    @ApiModelProperty("主订单号")
    private String mainOrderNo;

    @ApiModelProperty("是否已退款0否1是")
    private Boolean isRefund;

    @ApiModelProperty("app_id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
