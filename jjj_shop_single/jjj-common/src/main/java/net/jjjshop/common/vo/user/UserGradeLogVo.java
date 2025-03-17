package net.jjjshop.common.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.user.UserGradeLog;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("用户等级日志VO")
public class UserGradeLogVo extends UserGradeLog {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户昵称")
    private String nickName;

    @ApiModelProperty("微信头像")
    private String avatarurl;

}
