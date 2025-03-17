package net.jjjshop.shop.vo.statistics;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.order.OrderRefund;

@Data
@Accessors(chain = true)
@ApiModel("商品退货信息排行VO")
public class ProductRefundRankingVo extends OrderRefund {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("总销售额")
    private Long refundCount;

    @ApiModelProperty("商品图片")
    private String imagePath;

    @ApiModelProperty("商品Id")
    private Integer productId;

    @ApiModelProperty("商品名称")
    private String productName;
}
