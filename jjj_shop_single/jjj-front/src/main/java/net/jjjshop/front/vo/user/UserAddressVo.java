package net.jjjshop.front.vo.user;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("文章VO")
public class UserAddressVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private Integer addressId;

    @ApiModelProperty("新市辖区(该字段用于记录region表中没有的市辖区)")
    private String district;

    @ApiModelProperty("收货人姓名")
    private String name;

    @ApiModelProperty("联系电话")
    private String phone;

    @ApiModelProperty("详细地址")
    private String detail;

    @ApiModelProperty("所在省份id")
    private Integer provinceId;

    @ApiModelProperty("所在城市id")
    private Integer cityId;

    @ApiModelProperty("所在区id")
    private Integer regionId;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("详细地区")
    private JSONObject region;
}
