package net.jjjshop.common.entity.settings;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 退货地址记录表
 *
 * @author jjjshop
 * @since 2022-07-20
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_return_address")
@ApiModel(value = "ReturnAddress对象")
public class ReturnAddress implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("退货地址id")
    @TableId(value = "address_id", type = IdType.AUTO)
    private Integer addressId;

    @ApiModelProperty("收货人姓名")
    private String name;

    @ApiModelProperty("联系电话")
    private String phone;

    @ApiModelProperty("详细地址")
    private String detail;

    @ApiModelProperty("排序 (数字越小越靠前)")
    private Integer sort;

    @ApiModelProperty("是否删除0=显示1=隐藏")
    @TableLogic
    private Integer isDelete;

    @ApiModelProperty("小程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
