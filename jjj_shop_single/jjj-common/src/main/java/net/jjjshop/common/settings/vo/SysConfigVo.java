package net.jjjshop.common.settings.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("系统设置VO")
public class SysConfigVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String shopName;
    private String shopBgImg;

    public SysConfigVo(){
        this.shopName = "三勾商城管理系统";
        this.shopBgImg = "";
    }
}
