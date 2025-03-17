package net.jjjshop.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.jjjshop.common.entity.page.Page;
import net.jjjshop.common.util.diy.DefaultItems;
import net.jjjshop.config.properties.SpringBootJjjProperties;

public class PageUtils {

    public static JSONObject getDefaultPage(){
        JSONObject pageData = new JSONObject();
        JSONObject page = new JSONObject();
        page.put("type", "page");
        page.put("name", "页面设置");
        // 参数
        JSONObject params = new JSONObject();
        params.put("name", "页面设置");
        params.put("title", "页面标题");
        params.put("titleType", "text"); //text文字 image图片
        params.put("shareTitle", "分享标题");
        params.put("shareImg", "");
        params.put("toplogo", "");
        page.put("params", params);
        // 样式
        JSONObject style = new JSONObject();
        style.put("titleTextColor", "black");
        style.put("titleBackgroundColor", "#ffffff");
        page.put("style", style);
        // 分类设置
        JSONObject category = new JSONObject();
        category.put("open", 0);
        category.put("color", "#000000");
        page.put("category", category);
        //pageData
        pageData.put("page", page);
        pageData.put("items", new JSONArray());
        return pageData;
    }

    //生成默认page
    public static Page getPage(Integer pageType){
        Page page = new Page();
        page.setIsDefault(true);
        page.setPageType(pageType);
        JSONObject pageData = new JSONObject();
        JSONArray items = new JSONArray();
        JSONObject defaultPage = new JSONObject();
        //页面类型(10首页 20自定义页 30个人中心)
        if(pageType == 10){
            page.setPageName("首页");
            items = DefaultItems.getDefaultItemsArray(SpringBootJjjProperties.getStaticAccessUrl());
            defaultPage = getDefaultPage();
        }
        pageData.put("items",items);
        pageData.put("page",defaultPage.get("page"));
        page.setPageData(pageData.toJSONString());
        return page;
    }
}
