package net.jjjshop.front.param.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "OrderRefundDeliveryParam", description = "售后单配送信息对象")
public class OrderRefundDeliveryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("物流编号")
    private String expressNo;

    @ApiModelProperty("物流公司Id")
    private Integer expressId;

    @ApiModelProperty("售后单Id")
    private Integer orderRefundId;

}
