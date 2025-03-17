

package net.jjjshop.front.vo.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.entity.product.ProductComment;
import net.jjjshop.common.entity.product.ProductSpec;
import net.jjjshop.common.entity.settings.DeliveryRule;
import net.jjjshop.common.vo.product.ProductImageVo;
import net.jjjshop.common.vo.product.ProductSkuVo;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商品详情VO")
public class ProductVo extends Product {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("图片")
    private List<ProductImageVo> image;

    @ApiModelProperty("详情图片")
    private List<ProductImageVo> contentImage;

    @ApiModelProperty("商品sku")
    private List<ProductSkuVo> skuList;

    @ApiModelProperty("商品sku")
    private ProductSkuVo productSku;

    @ApiModelProperty("商品spec")
    private List<ProductSpec> specList;

    @ApiModelProperty("物流规则")
    private DeliveryRule deliveryRule;

    @ApiModelProperty("商品评论")
    private List<ProductComment> commentData;

    @ApiModelProperty("商品销量")
    private Integer productSales;

    @ApiModelProperty("商品视频封面")
    private String videoFilePath;

    @ApiModelProperty("商品视频路径")
    private String posterFilePath;

    @ApiModelProperty("是否会员折扣")
    private Boolean isUserGrade;
}
