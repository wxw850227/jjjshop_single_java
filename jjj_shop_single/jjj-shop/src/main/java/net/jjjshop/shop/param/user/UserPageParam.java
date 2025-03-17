

package net.jjjshop.shop.param.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

import java.util.Date;

/**
 * 部门 查询参数对象
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "用户查询分页参数", description = "用户查询分页参数")
public class UserPageParam extends BasePageOrderParam {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("等级id")
    private Integer gradeId;

    @ApiModelProperty("搜索参数")
    private String search;

    @ApiModelProperty("微信昵称")
    private String nickName;

    @ApiModelProperty("性别")
    private Integer sex;

    @ApiModelProperty("标签")
    private Integer tagId;

    @ApiModelProperty("开始时间")
    private Date startDate;

    @ApiModelProperty("结束时间")
    private Date endDate;
}
