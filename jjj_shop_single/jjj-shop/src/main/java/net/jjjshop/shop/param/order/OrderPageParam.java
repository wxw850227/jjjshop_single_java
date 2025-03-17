

package net.jjjshop.shop.param.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

/**
 * 部门 查询参数对象
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OrderPageParam对象", description = "订单分页参数")
public class OrderPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("搜索订单号")
    private String orderNo;

    @ApiModelProperty("查询条件：起始日期")
    private String startDate;

    @ApiModelProperty("查询条件：结束日期")
    private String endDate;

    @ApiModelProperty("查询条件：配送方式")
    private Integer deliveryType;

    @ApiModelProperty("查询条件：订单状态")
    private String dataType;
}
