package net.jjjshop.shop.vo.product;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.product.ProductComment;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商品评价VO")
public class CommentVo extends ProductComment {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品图片主图")
    private String imagePath;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品一口价")
    private BigDecimal productPrice;

    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("微信昵称")
    @TableField("nickName")
    private String nickname;

    @ApiModelProperty("评价图片")
    private List<String> image;

}
