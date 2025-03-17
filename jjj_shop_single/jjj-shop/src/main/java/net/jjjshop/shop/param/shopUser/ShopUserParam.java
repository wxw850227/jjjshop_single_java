

package net.jjjshop.shop.param.shopUser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 部门 查询参数对象
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ShopUser对象", description = "ShopUser对象")
public class ShopUserParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Integer shopUserId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("登录密码")
    private String password;

    @ApiModelProperty("确认密码")
    private String confirmPassword;

    @ApiModelProperty("姓名")
    private String realName;

    @ApiModelProperty("用户角色")
    private List<Integer> roleId;
}
