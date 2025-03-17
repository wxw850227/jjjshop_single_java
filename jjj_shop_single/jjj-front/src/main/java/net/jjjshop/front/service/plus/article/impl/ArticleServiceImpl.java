package net.jjjshop.front.service.plus.article.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.plus.article.Article;
import net.jjjshop.common.mapper.plus.article.ArticleMapper;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.front.param.plus.article.ArticlePageParam;
import net.jjjshop.front.service.plus.article.ArticleCategoryService;
import net.jjjshop.front.service.plus.article.ArticleService;
import net.jjjshop.front.vo.plus.article.ArticleDetailVo;
import net.jjjshop.front.vo.plus.article.ArticleListVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 文章记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-25
 */
@Slf4j
@Service
public class ArticleServiceImpl extends BaseServiceImpl<ArticleMapper, Article> implements ArticleService {


    @Autowired
    private UploadFileUtils uploadFileUtils;

    @Autowired
    private ArticleCategoryService articleCategoryService;

    /**
     * 文章分页列表显示
     * @param pageIndex
     * @param pageSize
     * @param categoryId
     * @return
     */
    public Paging<ArticleListVo> getList(Integer pageIndex, Integer pageSize, Integer categoryId) {
        //转换为分页对象
        ArticlePageParam articlePageParam = new ArticlePageParam();
        articlePageParam.setPageIndex(Long.valueOf(pageIndex));
        articlePageParam.setPageSize(Long.valueOf(pageSize));
        articlePageParam.setCategoryId(categoryId);
        Page page = new PageInfo(articlePageParam);
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Article::getArticleStatus, 1);
        wrapper.eq(Article::getIsDelete, 0);
        //如果传入的分类Id不为空则根据文章分类进行查询
        if (articlePageParam.getCategoryId() != null && articlePageParam.getCategoryId() > 0) {
            wrapper.eq(Article::getCategoryId, articlePageParam.getCategoryId());
        }
        wrapper.orderByAsc(Article::getArticleSort);
        wrapper.orderByDesc(Article::getCreateTime);
        IPage<Article> iPage = this.page(page, wrapper);
        IPage<ArticleListVo> result = iPage.convert(item -> {
            ArticleListVo vo = new ArticleListVo();
            BeanUtils.copyProperties(item, vo);
            vo.setFilePath(uploadFileUtils.getFilePath(item.getImageId()));
            vo.setViewTime(item.getActualViews() + item.getVirtualViews());
            return vo;
        });
        return new Paging(result);
    }

    /**
     * 文章详情
     * @param articleId
     * @return
     */
    public ArticleDetailVo getDetail(Integer articleId) {
        Article article = this.getById(articleId);
        ArticleDetailVo vo = new ArticleDetailVo();
        BeanUtils.copyProperties(article, vo);
        vo.setCategoryName(articleCategoryService.getById(vo.getCategoryId()).getName());
        vo.setFilePath(uploadFileUtils.getFilePath(article.getImageId()));
        vo.setViewTime(article.getActualViews() + article.getVirtualViews());
        return vo;
    }
}
