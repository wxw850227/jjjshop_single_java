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
@ApiModel(value = "UserCouponPageParam", description = "用户优惠券分页查询参数")
public class UserCouponPageParam  extends BasePageOrderParam {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("优惠券种类")
    private String dataType;

}
