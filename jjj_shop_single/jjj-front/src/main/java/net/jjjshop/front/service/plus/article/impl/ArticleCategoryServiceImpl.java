package net.jjjshop.front.service.plus.article.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.plus.article.ArticleCategory;
import net.jjjshop.common.mapper.plus.article.ArticleCategoryMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.front.service.plus.article.ArticleCategoryService;
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

    /**
     * 获取所有文章分类
     * @param
     * @return
     */
    public List<ArticleCategory> getAll(){
        return this.list(new LambdaQueryWrapper<ArticleCategory>().orderByAsc(ArticleCategory::getSort,ArticleCategory::getCreateTime));
    }

}
