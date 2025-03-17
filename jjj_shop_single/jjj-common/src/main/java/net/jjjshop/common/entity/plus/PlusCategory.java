package net.jjjshop.common.entity.plus;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.validator.groups.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 插件分类表
 *
 * @author jjjshop
 * @since 2022-06-22
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_plus_category")
@ApiModel(value = "PlusCategory对象")
public class PlusCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("插件分类id")
    @TableId(value = "plus_category_id", type = IdType.AUTO)
    private Integer plusCategoryId;

    @NotBlank(message = "插件分类名称不能为空")
    @ApiModelProperty("插件分类名称")
    private String name;

    @ApiModelProperty("分类图片id")
    private Integer imageId;

    @ApiModelProperty("排序方式(数字越小越靠前)")
    private Integer sort;

    @ApiModelProperty("应用id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
