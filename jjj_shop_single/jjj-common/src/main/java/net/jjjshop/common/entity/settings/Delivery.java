package net.jjjshop.common.entity.settings;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 配送模板主表
 *
 * @author jjjshop
 * @since 2022-06-29
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_delivery")
@ApiModel(value = "Delivery对象")
public class Delivery implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("模板id")
    @TableId(value = "delivery_id", type = IdType.AUTO)
    private Integer deliveryId;

    @ApiModelProperty("模板名称")
    private String name;

    @ApiModelProperty("计费方式(10按件数 20按重量)")
    private Integer method;

    @ApiModelProperty("小程序d")
    private Integer appId;

    @ApiModelProperty("排序方式(数字越小越靠前)")
    private Integer sort;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
