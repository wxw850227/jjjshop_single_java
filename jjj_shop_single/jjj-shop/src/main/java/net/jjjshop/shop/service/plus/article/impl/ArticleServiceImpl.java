package net.jjjshop.shop.service.plus.article.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.plus.article.Article;
import net.jjjshop.common.entity.plus.article.ArticleCategory;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.common.mapper.plus.article.ArticleMapper;
import net.jjjshop.shop.param.plus.article.ArticlePageParam;
import net.jjjshop.shop.param.plus.article.ArticleParam;
import net.jjjshop.shop.service.plus.article.ArticleCategoryService;
import net.jjjshop.shop.service.plus.article.ArticleService;
import net.jjjshop.shop.vo.plus.article.ArticleVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 文章记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-25
 */
@Slf4j
@Service
public class ArticleServiceImpl extends BaseServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleCategoryService articleCategoryService;

    @Autowired
    private UploadFileUtils uploadFileUtils;

    /**
     * 文章分页列表
     * @param articlePageParam
     * @return
     */
    public Paging<ArticleVo> getList(ArticlePageParam articlePageParam) {
        Page<Article> page = new PageInfo(articlePageParam);
        IPage<Article> iPage = this.page(page, new LambdaQueryWrapper<Article>()
                .eq(Article::getIsDelete, 0)
                .orderByAsc(Article::getArticleSort)
                .orderByDesc(Article::getCreateTime));
        IPage<ArticleVo> result = iPage.convert(item -> {
            //如果添加文章时没有选择分类就会导致item.getCategoryId()==0,从而导致ArticleCategory ac为空,报空指针异常
            if(item.getCategoryId()!=0&&item.getCategoryId()!=null){
                ArticleCategory ac = articleCategoryService.getById(item.getCategoryId());
                ArticleVo vo = new ArticleVo();
                BeanUtils.copyProperties(item, vo);
                vo.setImageUrl(uploadFileUtils.getFilePath(vo.getImageId()));
                vo.setCategoryName(ac.getName());
                return vo;
            }else {
                ArticleVo vo = new ArticleVo();
                BeanUtils.copyProperties(item, vo);
                vo.setCategoryName("");
                return vo;
            }
        });
        return new Paging(result);
    }


    /**
     * 获取所有文章分类
     * @param
     * @return
     */
    public List<ArticleCategory> toAdd() {
        return articleCategoryService.list(new LambdaQueryWrapper<ArticleCategory>().select(ArticleCategory::getCategoryId, ArticleCategory::getName));
    }

    /**
     * 添加文章
     * @param articleParam
     * @return
     */
    public Boolean add(ArticleParam articleParam) {
        if (StringUtils.isEmpty(articleParam.getArticleContent())) {
            throw new BusinessException("请输入文章内容");
        }
        Article article = new Article();
        BeanUtils.copyProperties(articleParam, article);
        return this.save(article);
    }

    /**
     * 获取修改文章的vo
     * @param articleId
     * @return
     */
    public ArticleVo toEdit(Integer articleId) {
        Article article = this.getById(articleId);
        ArticleVo vo = new ArticleVo();
        BeanUtils.copyProperties(article, vo);
        vo.setCategoryName(articleCategoryService.getById(article.getCategoryId()).getName());
        vo.setImageUrl(uploadFileUtils.getFilePath(vo.getImageId()));
        return vo;
    }

    /**
     * 修改文章
     * @param articleParam
     * @return
     */
    public Boolean edit(ArticleParam articleParam) {
        if (StringUtils.isEmpty(articleParam.getArticleContent())) {
            throw new BusinessException("请输入文章内容");
        }
        Article article = new Article();
        BeanUtils.copyProperties(articleParam, article);
        return this.updateById(article);
    }

    /**
     * 软删除文章
     * @param articleId
     * @return
     */
    public Boolean setDelete(Integer articleId) {
        return this.update(new LambdaUpdateWrapper<Article>().eq(Article::getArticleId, articleId).set(Article::getIsDelete, 1));
    }
}
