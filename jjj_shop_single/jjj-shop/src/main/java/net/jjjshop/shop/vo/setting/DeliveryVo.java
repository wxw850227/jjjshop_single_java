package net.jjjshop.shop.vo.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.settings.Delivery;
import net.jjjshop.common.entity.settings.DeliveryRule;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("配送模板VO")
public class DeliveryVo extends Delivery {

    private static final long serialVersionUID = 1L;

    //编辑使用
    @ApiModelProperty("运费规则")
    private List<DeliveryRule> rule;
}
