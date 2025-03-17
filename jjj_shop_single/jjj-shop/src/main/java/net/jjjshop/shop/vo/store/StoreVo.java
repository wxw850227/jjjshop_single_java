package net.jjjshop.shop.vo.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.store.Store;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("门店VO")
public class StoreVo extends Store {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("地址坐标")
    private String coordinate;

    @ApiModelProperty("商家图片")
    private String logoImagePath;

    @ApiModelProperty("核销状态")
    private String isCheckText;

    @ApiModelProperty("支持状态")
    private String statusText;
}
