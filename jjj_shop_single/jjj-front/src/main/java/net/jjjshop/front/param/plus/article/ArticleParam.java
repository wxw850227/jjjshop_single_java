package net.jjjshop.front.param.plus.article;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "ArticleParam对象", description = "文章参数对象")
public class ArticleParam implements Serializable {

    private final static long serialVersionUID = 1L;

    @ApiModelProperty("文章id")
    @TableId(value = "article_id", type = IdType.AUTO)
    private Integer articleId;

    @ApiModelProperty("文章标题")
    private String articleTitle;

    @ApiModelProperty("列表显示方式(10小图展示 20大图展示)")
    private Integer showType;

    @ApiModelProperty("文章分类id")
    private Integer categoryId;

    @ApiModelProperty("封面图id")
    private Integer imageId;

    @ApiModelProperty("文章内容")
    private String articleContent;

    @ApiModelProperty("文章排序(数字越小越靠前)")
    private Integer articleSort;

    @ApiModelProperty("文章状态(0隐藏 1显示)")
    private Integer articleStatus;

    @ApiModelProperty("虚拟阅读量(仅用作展示)")
    private Integer virtualViews;

    @ApiModelProperty("实际阅读量")
    private Integer actualViews;

    @ApiModelProperty("是否删除")
    private Integer isDelete;

    @ApiModelProperty("小程序id")
    private Integer appId;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("更新时间")
    private String updateTime;

    @ApiModelProperty("描述")
    private String description;

}
