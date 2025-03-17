package net.jjjshop.front.service.plus.article;

import net.jjjshop.common.entity.plus.article.Article;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.front.vo.plus.article.ArticleDetailVo;
import net.jjjshop.front.vo.plus.article.ArticleListVo;

/**
 * 文章记录表 服务类
 * @author jjjshop
 * @since 2022-07-25
 */
public interface ArticleService extends BaseService<Article> {

    /**
     * 文章分页列表显示
     * @param pageIndex
     * @param pageSize
     * @param categoryId
     * @return
     */
    Paging<ArticleListVo> getList(Integer pageIndex, Integer pageSize, Integer categoryId);

    /**
     * 文章详情
     * @param articleId
     * @return
     */
    ArticleDetailVo getDetail(Integer articleId);
}
