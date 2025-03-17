package net.jjjshop.shop.vo.store;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.store.StoreClerk;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("门店店员VO")
public class StoreClerkVo extends StoreClerk {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("微信昵称")
    private String nickName;

    @ApiModelProperty("微信头像")
    private String avatarUrl;

    @ApiModelProperty("店名")
    private String storeName;

    @ApiModelProperty("店员状态")
    private String statusText;
}
