package net.jjjshop.shop.service.statistics.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.shop.service.order.OrderProductService;
import net.jjjshop.shop.service.order.OrderRefundService;
import net.jjjshop.shop.service.order.OrderService;
import net.jjjshop.shop.service.product.ProductService;
import net.jjjshop.shop.service.statistics.OrderRankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单统计数据 服务实现类
 * @author jjjshop
 * @since 2022-06-28
 */

@Slf4j
@Service
public class OrderRankingServiceImpl implements OrderRankingService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRefundService orderRefundService;

    @Autowired
    private OrderProductService orderProductService;

    @Autowired
    private ProductService productService;

    /**
     * 获取订单统计数据
     * @param
     * @return
     */
    //获取订单统计数据
    public JSONObject getData() throws ParseException {
        //获取今天时间
        String today = DateUtil.format(DateUtil.offsetDay(new Date(), 0), "yyyy-MM-dd");
        //获取昨天时间
        String yesterday = DateUtil.format(DateUtil.offsetDay(new Date(), -1), "yyyy-MM-dd");
        // 成交额(元)
        BigDecimal orderTotalPriceToday = orderService.getOrderData(today, null, "order_total_price");
        BigDecimal orderTotalPriceYesterday = orderService.getOrderData(yesterday, null, "order_total_price");
        // 下单用户数
        BigDecimal orderUserTotalToday = orderService.getOrderData(today, null, "order_user_total");
        BigDecimal orderUserTotalYesterday = orderService.getOrderData(yesterday, null, "order_user_total");
        // 支付订单数
        BigDecimal orderTotalToday = orderService.getOrderData(today, null, "order_total");
        BigDecimal orderTotalYesterday = orderService.getOrderData(yesterday, null, "order_total");
        // 退款成功总金额
        BigDecimal orderRefundMoneyToday = orderRefundService.getRefundData(today, null, "order_refund_money");
        BigDecimal orderRefundMoneyYesterday = orderRefundService.getRefundData(yesterday, null, "order_refund_money");
        // 退款成功订单数
        BigDecimal orderRefundTotalToday = orderRefundService.getRefundData(today, null, "order_refund_total");
        BigDecimal orderRefundTotalYesterday = orderRefundService.getRefundData(yesterday, null, "order_refund_total");
        // 客单价
        BigDecimal orderPerPriceToday = BigDecimal.ZERO;
        BigDecimal orderPerPriceYesterday = BigDecimal.ZERO;
        if (orderUserTotalToday.compareTo(BigDecimal.ZERO) > 0) {
            orderPerPriceToday = orderTotalPriceToday.divide(orderUserTotalToday,2,BigDecimal.ROUND_DOWN);
        }
        if (orderUserTotalYesterday.compareTo(BigDecimal.ZERO) > 0) {
            orderPerPriceYesterday = orderTotalPriceYesterday.divide(orderUserTotalYesterday,2,BigDecimal.ROUND_DOWN);
        }

        JSONObject json = new JSONObject();
        json.put("orderTotalPriceToday", orderTotalPriceToday);
        json.put("orderTotalPriceYesterday", orderTotalPriceYesterday);

        json.put("orderUserTotalToday", Integer.parseInt(orderUserTotalToday.toString()));
        json.put("orderUserTotalYesterday", Integer.parseInt(orderUserTotalYesterday.toString()));

        json.put("orderTotalToday", Integer.parseInt(orderTotalToday.toString()));
        json.put("orderTotalYesterday", Integer.parseInt(orderTotalYesterday.toString()));

        json.put("orderRefundMoneyToday", orderRefundMoneyToday);
        json.put("orderRefundMoneyYesterday", orderRefundMoneyYesterday);

        json.put("orderRefundTotalToday", Integer.parseInt(orderRefundTotalToday.toString()));
        json.put("orderRefundTotalYesterday", Integer.parseInt(orderRefundTotalYesterday.toString()));

        json.put("orderPerPriceToday", orderPerPriceToday);
        json.put("orderPerPriceYesterday", orderPerPriceYesterday);

        return json;
    }

    /**
     * 获取商品统计数据
     * @param
     * @return
     */
    //获取商品统计数据
    public JSONObject getProductData() throws ParseException {
        //获取今天时间
        String today = DateUtil.format(DateUtil.offsetDay(new Date(), 0), "yyyy-MM-dd");
        //获取昨天时间
        String yesterday = DateUtil.format(DateUtil.offsetDay(new Date(), -1), "yyyy-MM-dd");

        Integer saleToday = productService.count(new LambdaQueryWrapper<Product>().eq(Product::getIsDelete, 0).eq(Product::getProductStatus, 10));
        String saleYesterday = "--";
        Integer noPayToday = orderProductService.getOrderProductData(today, null, "no_pay");
        Integer noPayYesterday = orderProductService.getOrderProductData(yesterday, null, "no_pay");
        Integer payToday = orderProductService.getOrderProductData(today, null, "pay");
        Integer payYesterday = orderProductService.getOrderProductData(yesterday, null, "pay");

        JSONObject json = new JSONObject();
        json.put("saleToday", saleToday);
        json.put("saleYesterday", saleYesterday);
        json.put("noPayToday", noPayToday);
        json.put("noPayYesterday", noPayYesterday);
        json.put("payToday", payToday);
        json.put("payYesterday", payYesterday);
        return json;
    }

    /**
     * 通过时间范围获取商品统计数据
     * @param
     * @return
     */
    //通过时间范围获取商品统计数据
    public Map<String, Object> getOrderDataByDate(String startDate, String endDate,String type) throws ParseException {
        Map<String, Object> map = new HashMap<>();
        Date startTime = DateUtil.parse(startDate);
        Date endTime = DateUtil.parse(endDate);
        //endTime加一天
        endTime = DateUtil.offsetDay(endTime,1);
        List<JSONObject> data = new ArrayList<>();
        List<String> days = new ArrayList<>();
        for (Date t = startTime; t.before(endTime); t = DateUtil.offsetDay(t,1)) {
            String day = DateUtil.format(t, "yyyy-MM-dd");
            Integer totalNum = 0;
            if("refund".equals(type)){
                //获取退款订单数
                totalNum = Integer.parseInt(orderRefundService.getRefundData(day, null, "order_refund_total").toString());
            }else {
                //订单数据
                totalNum = Integer.parseInt(orderService.getOrderData(day, null, "order_total").toString());
            }
            BigDecimal totalMoney = BigDecimal.ZERO;
            if (totalNum!=0) {
                if("refund".equals(type)){
                    //获取退款金额
                    totalMoney = orderRefundService.getRefundData(day, null, "order_refund_money");
                }else {
                    //订单数据
                    totalMoney = orderService.getOrderData(day, null, "order_total_price");
                }
            }
            JSONObject json = new JSONObject();
            json.put("day", day);
            json.put("totalNum", totalNum);
            json.put("totalMoney", totalMoney);
            days.add(day);
            data.add(json);
        }
        map.put("data", data);
        map.put("days", days);
        return map;
    }

    /**
     * 获取订单总数
     * @param
     * @return
     */
    public BigDecimal getOrderTotal() throws ParseException {
        return orderService.getOrderData(null, null, "order_total");
    }

    /**
     * 获取用户已付款订单总数
     * @param startDate
     * @return
     */
    public Integer getPayOrderUserTotal(String startDate) throws ParseException {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.ge(Order::getPayTime, DateUtil.parse(startDate+" 00:00:00"));
        //如果结束查询时间为空,开始查询时间不为空，就默认设置时间查询区间为开始时间+1天
        wrapper.lt(Order::getPayTime,  DateUtil.parse(startDate+" 23:59:59"));
        wrapper.eq(Order::getIsDelete, 0);
        wrapper.eq(Order::getPayStatus, 20);
        List<Order> orderList = orderService.list(wrapper);
        List<Integer> idList = orderList.stream().map(Order::getUserId).collect(Collectors.toList());
        return new HashSet<>(idList).size();
    }

    /**
     * 通过时间范围查询商品统计数据
     * @param startDate,endDate
     * @return
     */
    public Map<String, Object> getProductDataByDate(String startDate, String endDate) throws ParseException {
        Map<String, Object> map = new HashMap<>();
        Date startTime = DateUtil.parse(startDate);
        Date endTime = DateUtil.parse(endDate);
        List<JSONObject> data = new ArrayList<>();
        List<String> days = new ArrayList<>();
        for (Date t = startTime; t.before(endTime); t = DateUtil.offsetDay(t,1)) {
            String day = DateUtil.format(t, "yyyy-MM-dd");
            Integer totalNum = orderProductService.getOrderProductData(day, null, "pay");
            JSONObject json = new JSONObject();
            json.put("day", day);
            json.put("totalNum", totalNum);
            days.add(day);
            data.add(json);
        }
        map.put("data", data);
        map.put("days", days);
        return map;
    }


}
