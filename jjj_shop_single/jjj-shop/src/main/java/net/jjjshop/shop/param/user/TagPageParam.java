package net.jjjshop.shop.param.user;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "用户标签分页对象", description = "用户标签分页对象")
public class TagPageParam extends BasePageOrderParam {
    private static final long serialVersionUID = 1L;
}
