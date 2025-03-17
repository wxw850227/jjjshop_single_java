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
 * 订单记录表
 *
 * @author jjjshop
 * @since 2022-07-04
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_order")
@ApiModel(value = "Order对象")
public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单id")
    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("交易流水号")
    private String tradeNo;

    @ApiModelProperty("商品总金额(不含优惠折扣)")
    private BigDecimal totalPrice;

    @ApiModelProperty("订单金额(含优惠折扣)")
    private BigDecimal orderPrice;

    @ApiModelProperty("优惠券id")
    private Integer couponId;

    @ApiModelProperty("优惠券抵扣金额")
    private BigDecimal couponMoney;

    @ApiModelProperty("实际付款金额(包含运费)")
    private BigDecimal payPrice;

    @ApiModelProperty("后台修改的订单金额（差价）")
    private BigDecimal updatePrice;

    @ApiModelProperty("买家留言")
    private String buyerRemark;

    @ApiModelProperty("支付方式(10余额支付 20微信支付)")
    private Integer payType;

    @ApiModelProperty("支付来源,mp,wx")
    private String paySource;

    @ApiModelProperty("付款状态(10未付款 20已付款)")
    private Integer payStatus;

    @ApiModelProperty("付款时间")
    private Date payTime;

    @ApiModelProperty("支付截止时间")
    private Date payEndTime;

    @ApiModelProperty("配送方式(10快递配送 20上门自提 30无需物流)")
    private Integer deliveryType;

    @ApiModelProperty("自提门店id")
    private Integer extractStoreId;

    @ApiModelProperty("核销店员id")
    private Integer extractClerkId;

    @ApiModelProperty("运费金额")
    private BigDecimal expressPrice;

    @ApiModelProperty("物流公司id")
    private Integer expressId;

    @ApiModelProperty("物流公司")
    private String expressCompany;

    @ApiModelProperty("物流单号")
    private String expressNo;

    @ApiModelProperty("发货状态(10未发货 20已发货)")
    private Integer deliveryStatus;

    @ApiModelProperty("发货时间")
    private Date deliveryTime;

    @ApiModelProperty("收货状态(10未收货 20已收货)")
    private Integer receiptStatus;

    @ApiModelProperty("收货时间")
    private Date receiptTime;

    @ApiModelProperty("订单状态10=>进行中，20=>已经取消，30=>已完成")
    private Integer orderStatus;

    @ApiModelProperty("订单是否已结算(0未结算 1已结算)")
    private Integer isSettled;

    @ApiModelProperty("微信支付交易号")
    private String transactionId;

    @ApiModelProperty("是否已评价(0否 1是)")
    private Integer isComment;

    @ApiModelProperty("订单来源(10普通 20积分 30拼团 40砍价 50秒杀 60礼包购)")
    private Integer orderSource;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("拼团等活动失败退款")
    private Integer isRefund;

    @ApiModelProperty("拼团状态 10拼单中 20拼单成功 30拼单失败")
    private Integer assembleStatus;

    @ApiModelProperty("活动id，对应拼团，秒杀，砍价活动id")
    private Integer activityId;

    @ApiModelProperty("商家取消订单备注")
    private String cancelRemark;

    @ApiModelProperty("是否自动发货1自动0手动")
    private Integer virtualAuto;

    @ApiModelProperty("虚拟物品内容")
    private String virtualContent;

    @ApiModelProperty("余额抵扣金额")
    private BigDecimal balance;

    @ApiModelProperty("在线支付金额")
    private BigDecimal onlineMoney;

    @ApiModelProperty("退款金额")
    private BigDecimal refundMoney;

    @ApiModelProperty("是否删除")
    private Integer isDelete;

    @ApiModelProperty("小程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
