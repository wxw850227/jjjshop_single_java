package net.jjjshop.common.entity.settings;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 商城设置记录表
 *
 * @author jjjshop
 * @since 2022-06-24
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_setting")
@ApiModel(value = "Setting对象")
public class Setting implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "设置项标示不能为空")
    @ApiModelProperty("设置项标示")
    @TableId(value = "set_key", type = IdType.INPUT)
    private String setKey;

    @NotBlank(message = "设置项描述不能为空")
    @ApiModelProperty("设置项描述")
    private String description;

    @NotBlank(message = "设置内容（json格式）不能为空")
    @ApiModelProperty("设置内容（json格式）")
    private String setValue;

    @ApiModelProperty("小程序id")
    private Integer appId;

    @ApiModelProperty("更新时间")
    private Integer updateTime;

}
