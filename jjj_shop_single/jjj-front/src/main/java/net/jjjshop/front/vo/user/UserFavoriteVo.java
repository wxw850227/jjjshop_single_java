package net.jjjshop.front.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.user.UserFavorite;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@ApiModel("登录用户信息TokenVO")
public class UserFavoriteVo extends UserFavorite {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("图片")
    private String productImage;

    @ApiModelProperty("商品销量")
    private Integer productId;

    @ApiModelProperty("商品名字")
    private String productName;

    @ApiModelProperty("商品价格")
    private BigDecimal productPrice;

    @ApiModelProperty("商品价格")
    private BigDecimal linePrice;
}
