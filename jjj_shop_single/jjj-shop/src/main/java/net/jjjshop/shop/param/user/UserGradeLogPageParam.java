package net.jjjshop.shop.param.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "用户等级日志分页对象", description = "用户等级日志分页对象")
public class UserGradeLogPageParam extends BasePageOrderParam {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户昵称")
    private String nickName;

}
