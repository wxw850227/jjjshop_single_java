package net.jjjshop.common.entity.app;

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
 * app升级记录表
 *
 * @author jjjshop
 * @since 2022-07-27
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_app_update")
@ApiModel(value = "AppUpdate对象")
public class AppUpdate implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId(value = "update_id", type = IdType.AUTO)
    private Integer updateId;

    @ApiModelProperty("appid")
    private Integer appId;

    @ApiModelProperty("android版本号")
    private String versionAndroid;

    @ApiModelProperty("ios版本号")
    private String versionIos;

    @ApiModelProperty("热更新包下载地址")
    private String wgtUrl;

    @ApiModelProperty("安卓整包升级地址")
    private String pkgUrlAndroid;

    @ApiModelProperty("ios整包升级地址")
    private String pkgUrlIos;

    @ApiModelProperty("是否删除")
    private Integer isDelete;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
