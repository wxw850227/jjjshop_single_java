package net.jjjshop.common.entity.store;

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
 * 商家门店店员表
 *
 * @author jjjshop
 * @since 2022-07-27
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_store_clerk")
@ApiModel(value = "StoreClerk对象")
public class StoreClerk implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("店员id")
    @TableId(value = "clerk_id", type = IdType.AUTO)
    private Integer clerkId;

    @ApiModelProperty("所属门店id")
    private Integer storeId;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("店员姓名")
    private String realName;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("状态(0禁用 1启用)")
    private Integer status;

    @ApiModelProperty("是否删除")
    private Integer isDelete;

    @ApiModelProperty("小程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
