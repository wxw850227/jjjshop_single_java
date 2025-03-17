package net.jjjshop.front.param.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UserFavoritePageParam", description = "用户收藏分页查询参数")
public class UserFavoritePageParam extends BasePageOrderParam {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("种类")
    private Integer type;

    @ApiModelProperty("用户Id")
    private Integer userId;

}
