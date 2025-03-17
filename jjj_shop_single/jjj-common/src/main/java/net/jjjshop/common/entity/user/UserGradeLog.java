package net.jjjshop.common.entity.user;

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
 * 用户会员等级变更记录表
 *
 * @author jjjshop
 * @since 2022-07-21
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_user_grade_log")
@ApiModel(value = "UserGradeLog对象")
public class UserGradeLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "log_id", type = IdType.AUTO)
    private Integer logId;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("变更前的等级id")
    private Integer oldGradeId;

    @ApiModelProperty("变更后的等级id")
    private Integer newGradeId;

    @ApiModelProperty("变更类型(10后台管理员设置 20自动升级)")
    private Integer changeType;

    @ApiModelProperty("管理员备注")
    private String remark;

    @ApiModelProperty("程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

}
