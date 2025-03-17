

package net.jjjshop.admin.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.settings.Region;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("插件列表VO")
public class RegionVo extends Region {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("provinceId")
    private String provinceId;

    @ApiModelProperty("provinceId")
    private String cityId;
}
