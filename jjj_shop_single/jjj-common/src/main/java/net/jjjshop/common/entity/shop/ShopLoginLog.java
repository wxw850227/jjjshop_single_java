package net.jjjshop.common.entity.shop;

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
 * 管理员登录记录表
 *
 * @author jjjshop
 * @since 2022-08-15
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_shop_login_log")
@ApiModel(value = "ShopLoginLog对象")
public class ShopLoginLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "login_log_id", type = IdType.AUTO)
    private Integer loginLogId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("登录ip")
    private String ip;

    @ApiModelProperty("登录结果")
    private String result;

    @ApiModelProperty("小程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

}
