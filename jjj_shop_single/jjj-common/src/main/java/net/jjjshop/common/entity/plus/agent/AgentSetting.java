package net.jjjshop.common.entity.plus.agent;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 分销商设置表
 *
 * @author jjjshop
 * @since 2022-06-29
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_agent_setting")
@ApiModel(value = "AgentSetting对象")
public class AgentSetting implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("设置项标示")
    @TableId(value = "set_key", type = IdType.AUTO)
    private String setKey;

    @ApiModelProperty("设置项描述")
    private String describe;

    @ApiModelProperty("设置内容(json格式)")
    private String setValues;

    @ApiModelProperty("小程序id")
    private Integer appId;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
