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
 * app应用记录表
 *
 * @author jjjshop
 * @since 2022-07-27
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("jjjshop_app_open")
@ApiModel(value = "AppOpen对象")
public class AppOpen implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("appid")
    @TableId(value = "app_id", type = IdType.AUTO)
    private Integer appId;

    @ApiModelProperty("应用AppID")
    private String openappId;

    @ApiModelProperty("应用AppSecret")
    private String openappSecret;

    @ApiModelProperty("微信商户号id")
    private String mchid;

    @ApiModelProperty("微信支付密钥")
    private String apikey;

    @ApiModelProperty("logo地址")
    private String logo;

    @ApiModelProperty("证书文件cert")
    private String certPem;

    @ApiModelProperty("证书文件key")
    private String keyPem;

    @ApiModelProperty("是否支持支付宝支付,0否1是")
    private Integer isAlipay;

    @ApiModelProperty("支付宝商户号")
    private String alipayAppid;

    @ApiModelProperty("支付宝公钥")
    private String alipayPublickey;

    @ApiModelProperty("应用私钥")
    private String alipayPrivatekey;

    @ApiModelProperty("是否回收")
    private Integer isRecycle;

    @ApiModelProperty("是否删除")
    private Integer isDelete;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

}
