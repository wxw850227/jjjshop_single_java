

package net.jjjshop.admin.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.settings.Region;

/**
 * 部门 查询参数对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "RegionParam对象", description = "region新增修改参数")
public class RegionParam extends Region {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("所属省份")
    private Integer provinceId;

    @ApiModelProperty("所属城市")
    private Integer cityId;
}
