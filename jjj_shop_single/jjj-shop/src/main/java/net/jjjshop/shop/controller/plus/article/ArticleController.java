package net.jjjshop.shop.controller.plus.article;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.plus.article.ArticleCategory;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.plus.article.ArticlePageParam;
import net.jjjshop.shop.param.plus.article.ArticleParam;
import net.jjjshop.shop.service.plus.article.ArticleCategoryService;
import net.jjjshop.shop.service.plus.article.ArticleService;
import net.jjjshop.shop.vo.plus.article.ArticleVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Api(value = "collection", tags = {"collection"})
@RestController
@RequestMapping("/shop/plus/article/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleCategoryService articleCategoryService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/plus/article/article/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Paging<ArticleVo>> index(@Validated @RequestBody ArticlePageParam articlePageParam) throws Exception {
        return ApiResult.ok(articleService.getList(articlePageParam));
    }

    @RequestMapping(value = "/toAdd", method = RequestMethod.GET)
    @RequiresPermissions("/plus/article/article/add")
    @OperationLog(name = "toAdd")
    @ApiOperation(value = "toAdd", response = String.class)
    public ApiResult<List<ArticleCategory>> toAdd() throws Exception {
        return ApiResult.ok(articleService.toAdd());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/plus/article/article/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@Validated @RequestBody ArticleParam articleParam) throws Exception {
        if (articleService.add(articleParam)) {
            return ApiResult.ok(null, "添加成功");
        } else {
            return ApiResult.fail("添加失败");
        }
    }

    @RequestMapping(value = "/toEdit", method = RequestMethod.GET)
    @RequiresPermissions("/plus/article/article/edit")
    @OperationLog(name = "toEdit")
    @ApiOperation(value = "toEdit", response = String.class)
    public ApiResult<Map<String, Object>> toEdit(Integer articleId) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("article", articleService.toEdit(articleId));
        map.put("categoryList", articleService.toAdd());
        return ApiResult.ok(map);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/plus/article/article/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@Validated @RequestBody ArticleParam articleParam) throws Exception {
        if (articleService.edit(articleParam)) {
            return ApiResult.ok(null, "修改成功");
        } else {
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/plus/article/article/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult delById(Integer articleId) {
        if (articleService.setDelete(articleId)) {
            return ApiResult.ok("删除成功");
        } else {
            return ApiResult.fail("删除失败");
        }
    }
}
