package net.jjjshop.shop.controller.plus.article;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.plus.article.ArticleCategory;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.plus.article.ArticleCategoryParam;
import net.jjjshop.shop.service.plus.article.ArticleCategoryService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(value = "collection", tags = {"collection"})
@RestController
@RequestMapping("/shop/plus/article/category")
public class ArticleCategoryController {

    @Autowired
    private ArticleCategoryService articleCategoryService;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/plus/article/category/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<List<ArticleCategory>> index() throws Exception {
        return ApiResult.ok(articleCategoryService.list());
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/plus/article/category/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@Validated @RequestBody ArticleCategoryParam articleCategoryParam) throws Exception {
        if (articleCategoryService.add(articleCategoryParam)) {
            return ApiResult.ok(null, "添加成功");
        } else {
            return ApiResult.fail("添加失败");
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/plus/article/category/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@Validated @RequestBody ArticleCategoryParam articleCategoryParam) throws Exception {
        if (articleCategoryService.edit(articleCategoryParam)) {
            return ApiResult.ok(null, "修改成功");
        } else {
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/plus/article/category/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delById(Integer id) throws Exception {
        if (articleCategoryService.delById(id)) {
            return ApiResult.ok(null, "删除成功");
        } else {
            return ApiResult.fail("删除失败");
        }
    }
}
