package net.jjjshop.front.param.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@Accessors(chain = true)
@ApiModel(value = "CategoryParam对象", description = "Category新增修改参数")
public class UserAddressParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "address_id", type = IdType.AUTO)
    private Integer addressId;

    @ApiModelProperty("电话号码")
    private String phone;

    @ApiModelProperty("详细地址")
    private String detail;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("省份Id")
    private Integer provinceId;

    @ApiModelProperty("城市Id")
    private Integer cityId;

    @ApiModelProperty("区域Id")
    private Integer regionId;


}
