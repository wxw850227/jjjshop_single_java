package net.jjjshop.shop.service.plus.article.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.plus.article.Article;
import net.jjjshop.common.entity.plus.article.ArticleCategory;
import net.jjjshop.common.mapper.plus.article.ArticleCategoryMapper;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.shop.param.plus.article.ArticleCategoryParam;
import net.jjjshop.shop.service.plus.article.ArticleCategoryService;
import net.jjjshop.shop.service.plus.article.ArticleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 文章分类表 服务实现类
 * @author jjjshop
 * @since 2022-07-25
 */
@Slf4j
@Service
public class ArticleCategoryServiceImpl extends BaseServiceImpl<ArticleCategoryMapper, ArticleCategory> implements ArticleCategoryService {

    @Autowired
    private ArticleService articleService;


    /**
     * 文章分类列表
     * @param
     * @return
     */
    public List<ArticleCategory> getList() {
        return this.list();
    }

    /**
     * 添加文章分类
     * @param articleCategoryParam
     * @return
     */
    public Boolean add(ArticleCategoryParam articleCategoryParam) {
        ArticleCategory articleCategory = new ArticleCategory();
        BeanUtils.copyProperties(articleCategoryParam, articleCategory);
        return this.save(articleCategory);
    }

    /**
     * 修改文章分类
     * @param articleCategoryParam
     * @return
     */
    public Boolean edit(ArticleCategoryParam articleCategoryParam) {
        ArticleCategory articleCategory = new ArticleCategory();
        BeanUtils.copyProperties(articleCategoryParam, articleCategory);
        return this.updateById(articleCategory);
    }

    /**
     * 真删除文章分类
     * @param id
     * @return
     */
    public Boolean delById(Integer id) {
        int count = articleService.count(new LambdaQueryWrapper<Article>()
                .eq(Article::getCategoryId, id)
                //是否删除
                .eq(Article::getIsDelete, 0)
        );
        if (count > 0) {
            throw new BusinessException("该分类下存在" + count + "个文章，不允许删除");
        }
        return this.removeById(id);
    }
}
