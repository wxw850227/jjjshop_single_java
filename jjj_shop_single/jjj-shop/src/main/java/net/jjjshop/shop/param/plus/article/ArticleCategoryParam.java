package net.jjjshop.shop.param.plus.article;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "ArticleParam对象", description = "文章参数对象")
public class ArticleCategoryParam implements Serializable {
    private final static long serialVersionUID = 1L;

    @ApiModelProperty("商品分类id")
    private Integer categoryId;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("排序方式(数字越小越靠前)")
    private Integer sort;

    @ApiModelProperty("小程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("更新时间")
    private String updateTime;

}
