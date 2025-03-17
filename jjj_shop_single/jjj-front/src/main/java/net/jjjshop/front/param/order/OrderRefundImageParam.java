package net.jjjshop.front.param.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "OrderRefundImageParam", description = "售后单图片参数")
public class OrderRefundImageParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("图片保存路径")
    private String filePath;

    @ApiModelProperty("图片Id")
    private Integer fileId;
}
