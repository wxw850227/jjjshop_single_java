package net.jjjshop.front.service.plus.article;

import net.jjjshop.common.entity.plus.article.ArticleCategory;
import net.jjjshop.framework.common.service.BaseService;

import java.util.List;

/**
 * 文章分类表 服务类
 * @author jjjshop
 * @since 2022-07-25
 */
public interface ArticleCategoryService extends BaseService<ArticleCategory> {

    /**
     * 获取所有文章分类
     * @param
     * @return
     */
    List<ArticleCategory> getAll();
}
