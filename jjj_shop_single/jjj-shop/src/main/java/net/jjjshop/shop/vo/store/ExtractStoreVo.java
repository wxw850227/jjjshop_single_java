package net.jjjshop.shop.vo.store;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("自提门店VO")
public class ExtractStoreVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("门店id")
    @TableId(value = "store_id", type = IdType.AUTO)
    private Integer storeId;

    @ApiModelProperty("门店名称")
    private String storeName;

    @ApiModelProperty("门店logo图片id")
    private Integer logoImageId;

    @ApiModelProperty("联系人")
    private String linkman;

    @ApiModelProperty("联系电话")
    private String phone;

    @ApiModelProperty("营业时间")
    private String shopHours;

    @ApiModelProperty("所在省份id")
    private Integer provinceId;

    @ApiModelProperty("所在城市id")
    private Integer cityId;

    @ApiModelProperty("所在辖区id")
    private Integer regionId;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("门店坐标经度")
    private String longitude;

    @ApiModelProperty("门店坐标纬度")
    private String latitude;

    @ApiModelProperty("geohash")
    private String geohash;

    @ApiModelProperty("门店简介")
    private String summary;

    @ApiModelProperty("门店排序(数字越小越靠前)")
    private Integer sort;

    @ApiModelProperty("是否支持自提核销(0否 1支持)")
    private Integer isCheck;

    @ApiModelProperty("门店状态(0禁用 1启用)")
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
