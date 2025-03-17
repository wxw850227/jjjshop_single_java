package net.jjjshop.common.mapper.plus.article;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.common.entity.plus.article.Article;
import org.springframework.stereotype.Repository;


/**
 * 文章记录表 Mapper 接口
 *
 * @author jjjshop
 * @since 2022-07-25
 */
@Repository
public interface ArticleMapper extends BaseMapper<Article> {
}
