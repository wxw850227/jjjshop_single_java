

package net.jjjshop.shop.param.product;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.validator.groups.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 部门 查询参数对象
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "ProductParam对象", description = "Product新增修改参数")
public class ProductParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull(message = "id不能为空", groups = {Update.class})
    @ApiModelProperty("产品id")
    private Integer productId;

    @NotBlank(message = "产品名称不能为空")
    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品一口价")
    private BigDecimal productPrice;

    @ApiModelProperty("产品编码")
    private String productNo;

    @ApiModelProperty("产品总库存")
    private Integer productStock;

    @ApiModelProperty("视频id")
    private Integer videoId;

    @ApiModelProperty("视频封面id")
    private Integer posterId;

    @ApiModelProperty("商品卖点")
    private String sellingPoint;

    @ApiModelProperty("产品分类id")
    private Integer categoryId;

    @ApiModelProperty("产品规格(10单规格 20多规格)")
    private Integer specType;

    @ApiModelProperty("库存计算方式(10下单减库存 20付款减库存)")
    private Integer deductStockType;

    @ApiModelProperty("产品详情")
    private String content;

    @ApiModelProperty("初始销量")
    private Integer salesInitial;

    @ApiModelProperty("实际销量")
    private Integer salesActual;

    @ApiModelProperty("产品排序(数字越小越靠前)")
    private Integer productSort;

    @ApiModelProperty("配送模板id")
    private Integer deliveryId;

    @ApiModelProperty("是否开启会员折扣(1开启 0关闭)")
    private Integer isEnableGrade;

    @ApiModelProperty("会员折扣设置(0默认等级折扣 1单独设置折扣)")
    private Integer isAloneGrade;

    @ApiModelProperty("单独设置折扣的配置")
    private String aloneGradeEquity;

    @ApiModelProperty("折扣金额类型(10百分比 20固定金额)")
    private Integer aloneGradeType;

    @ApiModelProperty("产品状态(10上架 20仓库中 30回收站)")
    private Integer productStatus;

    @ApiModelProperty("是否虚拟，0否1是")
    private Integer isVirtual;

    @ApiModelProperty("限购数量0为不限")
    private Integer limitNum;

    @ApiModelProperty("可购买会员等级id，逗号隔开")
    private String gradeIds;

    @ApiModelProperty("是否自动发货1自动0手动")
    private Integer virtualAuto;

    @ApiModelProperty("虚拟物品内容")
    private String virtualContent;

    @ApiModelProperty("详情是否纯图0否1是")
    private Integer isPicture;

    @NotNull(message = "图片集合id不能为空")
    @ApiModelProperty("图片集合id")
    private List<ImageParam> image;

    @ApiModelProperty("内容图片集合id")
    private List<ImageParam> contentImage;

    @ApiModelProperty("商品单规格sku属性")
    private SkuParam sku;

    @ApiModelProperty("场景，编辑还是复制")
    private String scene;

    @ApiModelProperty("多规格")
    private JSONObject specMany;

    @Data
    @Accessors(chain = true)
    @ApiModel("上传图片VO")
    public static class ImageParam implements Serializable{
        private static final long serialVersionUID = 1L;
        private Integer imageId;
        private String filePath;
        public ImageParam(){

        }
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("商品SKU")
    public static class SkuParam implements Serializable{
        private static final long serialVersionUID = 1L;
        private BigDecimal linePrice;
        private String productNo;
        private BigDecimal productPrice;
        private Double productWeight;
        private Integer stockNum;
        public SkuParam(){

        }
    }
}
