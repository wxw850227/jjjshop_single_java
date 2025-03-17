package net.jjjshop.shop.param.plus.article;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.framework.core.pagination.BasePageOrderParam;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ArticlePageParam对象", description = "文章分页查询参数")
public class ArticlePageParam extends BasePageOrderParam {
    private final static long serialVersionUID = 1L;
}
