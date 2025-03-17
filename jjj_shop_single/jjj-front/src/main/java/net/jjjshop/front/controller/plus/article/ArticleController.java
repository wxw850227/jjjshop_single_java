package net.jjjshop.front.controller.plus.article;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.plus.article.ArticleCategory;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.front.service.plus.article.ArticleCategoryService;
import net.jjjshop.front.service.plus.article.ArticleService;
import net.jjjshop.front.vo.plus.article.ArticleDetailVo;
import net.jjjshop.front.vo.plus.article.ArticleListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(value = "article", tags = {"文章管理"})
@RestController
@RequestMapping("/front/plus/article/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleCategoryService articleCategoryService;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Paging<ArticleListVo>> index(Integer pageIndex, Integer pageSize, Integer categoryId) {
        return ApiResult.ok(articleService.getList(pageIndex, pageSize, categoryId));
    }

    @RequestMapping(value = "/category", method = RequestMethod.GET)
    @OperationLog(name = "category")
    @ApiOperation(value = "category", response = String.class)
    public ApiResult<List<ArticleCategory>> category() {
        return ApiResult.ok(articleCategoryService.getAll());
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    @OperationLog(name = "detail")
    @ApiOperation(value = "detail", response = String.class)
    public ApiResult<ArticleDetailVo> detail(Integer articleId) {
        return ApiResult.ok(articleService.getDetail(articleId));
    }

}
