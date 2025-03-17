package net.jjjshop.shop.param.store;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "商户店员分页列表", description = "商户店员分页列表")
public class StoreClerkPageParam  extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("搜索栏内容")
    private String search;

    @ApiModelProperty("搜索栏店名Id")
    private Integer storeId;


}
