package net.jjjshop.shop.service.statistics;

import com.alibaba.fastjson.JSONObject;

import java.text.ParseException;

/**
 * 首页统计数据 服务类
 * @author jjjshop
 * @since 2022-06-28
 */

public interface HomeDataService {

    /**
     * 获取首页显示数据
     * @param
     * @return
     */
    JSONObject getHomeData() throws ParseException;
}
