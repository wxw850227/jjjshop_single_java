package net.jjjshop.common.entity.settings;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.validator.groups.Update;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 
 *
 * @author jjjshop
 * @since 2022-06-24
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_region")
@ApiModel(value = "Region对象")
public class Region implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("父id")
    private Integer pid;

    @ApiModelProperty("简称")
    private String shortname;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("全称")
    private String mergerName;

    @ApiModelProperty("层级 1 2 3 省市区县")
    private Integer level;

    @ApiModelProperty("拼音")
    private String pinyin;

    @ApiModelProperty("长途区号")
    private String code;

    @ApiModelProperty("邮编")
    private String zipCode;

    @ApiModelProperty("首字母")
    private String first;

    @ApiModelProperty("经度")
    private String lng;

    @ApiModelProperty("纬度")
    private String lat;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("是否删除0否1是")
    private Boolean isDelete;

}
