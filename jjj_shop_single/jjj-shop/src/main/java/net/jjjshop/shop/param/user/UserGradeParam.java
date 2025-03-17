package net.jjjshop.shop.param.user;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@ApiModel(value = "用户等级查询参数", description = "用户等级查询参数")
public class UserGradeParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("等级ID")
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
