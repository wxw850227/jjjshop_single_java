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
import java.math.BigDecimal;
import java.util.Date;

/**
 * 配送模板区域及运费表
 *
 * @author jjjshop
 * @since 2022-06-29
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_delivery_rule")
@ApiModel(value = "DeliveryRule对象")
public class DeliveryRule implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("规则id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("配送模板id")
    private Integer deliveryId;

    @ApiModelProperty("可配送区域(城市id集)")
    private String region;

    @ApiModelProperty("首件(个)/首重(Kg)")
    private Double first;

    @ApiModelProperty("运费(元)")
    private BigDecimal firstFee;

    @ApiModelProperty("续件/续重")
    private Double additional;

    @ApiModelProperty("续费(元)")
    private BigDecimal additionalFee;

    @ApiModelProperty("小程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

}
