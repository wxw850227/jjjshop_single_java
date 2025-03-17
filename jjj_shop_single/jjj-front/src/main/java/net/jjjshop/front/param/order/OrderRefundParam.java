package net.jjjshop.front.param.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.common.vo.setting.ImageVo;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "OrderRefundParam", description = "售后单参数对象")
public class OrderRefundParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单Id")
    private Integer orderProductId;

    @ApiModelProperty("用户售后申请说明")
    private String content;

    @ApiModelProperty("用户售后种类")
    private Integer type;

    @ApiModelProperty("售后订单上传图片")
    private List<ImageVo> images;

}
