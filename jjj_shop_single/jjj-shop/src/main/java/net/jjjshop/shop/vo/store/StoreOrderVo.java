package net.jjjshop.shop.vo.store;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.store.StoreOrder;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("订单核销VO")
public class StoreOrderVo extends StoreOrder {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("店名")
    private String storeName;

    @ApiModelProperty("店员姓名")
    private String realName;

    @ApiModelProperty("核销订单类型")
    private String orderTypeText;

    @ApiModelProperty("核销订单编号")
    private String orderNo;

}
