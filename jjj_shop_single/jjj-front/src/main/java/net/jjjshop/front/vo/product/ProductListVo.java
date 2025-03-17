

package net.jjjshop.front.vo.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("商品列表VO")
public class ProductListVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("产品id")
    private Integer productId;

    @NotBlank(message = "产品名称不能为空")
    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品一口价")
    private BigDecimal productPrice;

    @ApiModelProperty("划线价")
    private BigDecimal linePrice;

    @ApiModelProperty("产品编码")
    private String productNo;

    @ApiModelProperty("产品总库存")
    private Integer productStock;

    @ApiModelProperty("商品卖点")
    private String sellingPoint;

    @ApiModelProperty("产品分类id")
    private Integer categoryId;

    @ApiModelProperty("产品规格(10单规格 20多规格)")
    private Integer specType;

    @ApiModelProperty("销量")
    private Integer productSales;

    @ApiModelProperty("是否开启会员折扣(1开启 0关闭)")
    private Integer isEnableGrade;

    @ApiModelProperty("会员折扣设置(0默认等级折扣 1单独设置折扣)")
    private Integer isAloneGrade;

    @ApiModelProperty("是否虚拟，0否1是")
    private Integer isVirtual;

    @ApiModelProperty("单独设置折扣的配置")
    private String aloneGradeEquity;

    @ApiModelProperty("折扣金额类型(10百分比 20固定金额)")
    private Integer aloneGradeType;

    @ApiModelProperty("访问次数")
    private Integer viewTimes;

    @ApiModelProperty("限购数量0为不限")
    private Integer limitNum;

    @ApiModelProperty("可购买会员等级id，逗号隔开")
    private String gradeIds;

    @ApiModelProperty("是否会员折扣")
    private Boolean isUserGrade;

    // 附加字段
    @ApiModelProperty("图片")
    private String productImage;
}
