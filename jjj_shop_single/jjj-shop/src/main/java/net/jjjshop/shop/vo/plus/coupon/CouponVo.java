package net.jjjshop.shop.vo.plus.coupon;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.plus.coupon.Coupon;
import net.jjjshop.shop.vo.product.ProductVo;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("优惠券Vo")
public class CouponVo extends Coupon {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("开始时间文本")
    private String startTimeText;

    @ApiModelProperty("结束时间文本")
    private String endTimeText;

    @ApiModelProperty("限制商品id")
    private List<Integer> productShowIds;

    @ApiModelProperty("限制商品")
    private List<ProductVo> productList;

    @ApiModelProperty("限制分类")
    private JSONObject categoryList;
}
