package net.jjjshop.shop.service.plus.article;

import net.jjjshop.common.entity.plus.article.Article;
import net.jjjshop.common.entity.plus.article.ArticleCategory;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.plus.article.ArticlePageParam;
import net.jjjshop.shop.param.plus.article.ArticleParam;
import net.jjjshop.shop.vo.plus.article.ArticleVo;

import java.util.List;

/**
 * 文章记录表 服务类
 * @author jjjshop
 * @since 2022-07-25
 */
public interface ArticleService extends BaseService<Article> {

    /**
     * 文章分页列表
     * @param articlePageParam
     * @return
     */
    Paging<ArticleVo> getList(ArticlePageParam articlePageParam);

    /**
     * 获取所有文章分类
     * @param
     * @return
     */
    List<ArticleCategory> toAdd();

    /**
     * 添加文章
     * @param articleParam
     * @return
     */
    Boolean add(ArticleParam articleParam);

    /**
     * 获取修改文章的vo
     * @param articleId
     * @return
     */
    ArticleVo toEdit(Integer articleId);

    /**
     * 修改文章
     * @param articleParam
     * @return
     */
    Boolean edit(ArticleParam articleParam);

    /**
     * 软删除文章
     * @param articleId
     * @return
     */
    Boolean setDelete(Integer articleId);

}
