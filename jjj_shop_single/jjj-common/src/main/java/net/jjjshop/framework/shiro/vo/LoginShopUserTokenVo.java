

package net.jjjshop.framework.shiro.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.framework.shiro.service.LoginToken;

@Data
@Accessors(chain = true)
@ApiModel("登录用户信息TokenVO")
public class LoginShopUserTokenVo implements LoginToken {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("token")
    private String token;

    /**
     * 登录用户对象
     */
    private LoginShopUserVo loginShopUserVo;
}
