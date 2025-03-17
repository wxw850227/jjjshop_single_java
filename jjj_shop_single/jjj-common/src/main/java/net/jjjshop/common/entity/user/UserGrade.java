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
 * 用户会员等级表
 *
 * @author jjjshop
 * @since 2022-06-29
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_user_grade")
@ApiModel(value = "UserGrade对象")
public class UserGrade implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("等级ID")
    @TableId(value = "grade_id", type = IdType.AUTO)
    private Integer gradeId;

    @ApiModelProperty("等级名称")
    private String name;

    @ApiModelProperty("是否开放0，否1是")
    private Integer openMoney;

    @ApiModelProperty("升级条件")
    private Integer upgradeMoney;

    @ApiModelProperty("等级权益,百分比")
    private Integer equity;

    @ApiModelProperty("是否默认，1是，0否")
    private Integer isDefault;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("权重")
    private Integer weight;

    @ApiModelProperty("是否删除")
    private Integer isDelete;

    @ApiModelProperty("程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
