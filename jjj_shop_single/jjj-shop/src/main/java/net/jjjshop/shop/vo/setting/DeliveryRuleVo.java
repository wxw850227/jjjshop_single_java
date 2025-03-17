package net.jjjshop.shop.vo.setting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.settings.DeliveryRule;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("配送规则VO对象")
public class DeliveryRuleVo extends DeliveryRule {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("省份")
    private List<Integer> province;

    @ApiModelProperty("城市")
    private List<String> citys;
}
