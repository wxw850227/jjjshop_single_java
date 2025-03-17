package net.jjjshop.front.param.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OrderPageParam对象", description = "订单分页查询参数")
public class OrderPageParam extends BasePageOrderParam {

    private final static long serialVersionUID = 1L;

    @ApiModelProperty("订单分类")
    private String type;

    @ApiModelProperty("用户Id")
    private Integer userId;

}
