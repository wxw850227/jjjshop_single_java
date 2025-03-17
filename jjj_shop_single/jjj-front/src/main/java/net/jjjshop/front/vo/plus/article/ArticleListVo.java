package net.jjjshop.front.vo.plus.article;

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
@ApiModel("文章列表Vo")
public class ArticleListVo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("文章id")
    private Integer articleId;

    @ApiModelProperty("文章标题")
    private String articleTitle;

    @ApiModelProperty("列表显示方式(10小图展示 20大图展示)")
    private Integer showType;

    @ApiModelProperty("文章分类id")
    private Integer categoryId;

    @ApiModelProperty("文章排序(数字越小越靠前)")
    private Integer articleSort;

    @ApiModelProperty("文章状态(0隐藏 1显示)")
    private Integer articleStatus;

    @ApiModelProperty("虚拟阅读量(仅用作展示)")
    private Integer virtualViews;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("分类名称")
    private String categoryName;

    @ApiModelProperty("图片路径")
    private String filePath;

    @ApiModelProperty("浏览量")
    private Integer viewTime;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
