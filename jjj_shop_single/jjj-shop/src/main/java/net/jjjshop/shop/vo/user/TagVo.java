package net.jjjshop.shop.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.user.Tag;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("标签Vo")
public class TagVo extends Tag {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("使用该标签的会员人数")
    private Integer count;
}
