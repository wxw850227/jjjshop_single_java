package net.jjjshop.shop.service.statistics;

import com.alibaba.fastjson.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Map;

/**
 * 订单统计数据 服务类
 * @author jjjshop
 * @since 2022-06-28
 */

public interface OrderRankingService {

    /**
     * 获取订单统计数据
     * @param
     * @return
     */
    JSONObject getData() throws ParseException;

    /**
     * 获取商品统计数据
     * @param
     * @return
     */
    JSONObject getProductData() throws ParseException;

    /**
     * 通过时间范围获取商品统计数据
     * @param
     * @return
     */
    Map<String, Object> getOrderDataByDate(String startDate, String endDate,String type) throws ParseException;

    /**
     * 获取订单总数
     * @param
     * @return
     */
    BigDecimal getOrderTotal() throws ParseException;

    /**
     * 获取付款用户总数
     * @param startDate
     * @return
     */
    Integer getPayOrderUserTotal(String startDate) throws ParseException;

    /**
     * 通过时间范围查询商品统计数据
     * @param startDate,endDate
     * @return
     */
    Map<String, Object> getProductDataByDate(String startDate, String endDate) throws ParseException;
}
