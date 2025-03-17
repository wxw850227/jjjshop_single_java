package net.jjjshop.front.vo.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("订单自提门店VO")
public class ExtractStoreVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("门店名称")
    private String storeName;

    @ApiModelProperty("联系电话")
    private String phone;


    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("省份")
    private String province;

    @ApiModelProperty("城市")
    private String city;

    @ApiModelProperty("地区")
    private String region;
}
