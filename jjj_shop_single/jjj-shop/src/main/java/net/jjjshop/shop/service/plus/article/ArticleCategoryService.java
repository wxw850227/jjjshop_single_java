package net.jjjshop.shop.service.plus.article;

import net.jjjshop.common.entity.plus.article.ArticleCategory;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.shop.param.plus.article.ArticleCategoryParam;

import java.util.List;

/**
 * 文章分类表 服务类
 * @author jjjshop
 * @since 2022-07-25
 */
public interface ArticleCategoryService extends BaseService<ArticleCategory> {

    /**
     * 文章分类列表
     * @param
     * @return
     */
    List<ArticleCategory> getList();

    /**
     * 添加文章分类
     * @param articleCategoryParam
     * @return
     */
    Boolean add(ArticleCategoryParam articleCategoryParam);

    /**
     * 修改文章分类
     * @param articleCategoryParam
     * @return
     */
    Boolean edit(ArticleCategoryParam articleCategoryParam);

    /**
     * 真删除文章分类
     * @param id
     * @return
     */
    Boolean delById(Integer id);
}
