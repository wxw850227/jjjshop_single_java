package net.jjjshop.front.util.order.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "订单结算规则", description = "订单结算规则")
public class SettledRule implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty("优惠券抵扣")
    private Boolean isCoupon;

    @ApiModelProperty("会员等级折扣")
    private Boolean isUserGrade;

    public SettledRule(){
        this.isCoupon = true;
        this.isUserGrade = true;
    }
}
