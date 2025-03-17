package net.jjjshop.shop.service.statistics.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.shop.service.order.OrderRefundService;
import net.jjjshop.shop.service.order.OrderService;
import net.jjjshop.shop.service.product.ProductCommentService;
import net.jjjshop.shop.service.statistics.HomeDataService;
import net.jjjshop.shop.service.statistics.OrderRankingService;
import net.jjjshop.shop.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

/**
 * 首页统计数据 服务实现类
 * @author jjjshop
 * @since 2022-06-28
 */

@Slf4j
@Service
public class HomeDataServiceImpl implements HomeDataService {

    @Autowired
    private OrderRankingService orderRankingService;
    @Autowired
    private ProductRankingServiceImpl productRankingService;
    @Autowired
    private UserRankingServiceImpl userRankingService;
    @Autowired
    private ProductCommentService productCommentService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderRefundService orderRefundService;

    /**
     * 获取首页显示数据
     * @param
     * @return
     */
    public JSONObject getHomeData() throws ParseException {
        //获取今天时间
        String today = DateUtil.format(DateUtil.offsetDay(new Date(), 0), "yyyy-MM-dd");
        //获取昨天时间
        String yesterday = DateUtil.format(DateUtil.offsetDay(new Date(), -1), "yyyy-MM-dd");
        //获取七天前的时间
        String lately7days = DateUtil.format(DateUtil.offsetDay(new Date(), -7), "yyyy-MM-dd");

        // 商品总量
        Integer productTotal = productRankingService.getProductTotal();
        // 用户总量
        Integer userTotal = userRankingService.getUserTotal();
        // 订单总量
        Integer orderTotal = Integer.parseInt(orderRankingService.getOrderTotal().toString());
        // 评价总量
        Integer productCommentTotal = productCommentService.getProductCommentTotal();

        // 销售额(元)
        BigDecimal orderTotalPriceT = orderService.getOrderData(today, null, "order_total_price");
        BigDecimal orderTotalPriceY = orderService.getOrderData(yesterday, null, "order_total_price");

        // 支付订单数
        Integer orderTotalT = Integer.parseInt(orderService.getOrderData(today, null, "order_total").toString());
        Integer orderTotalY = Integer.parseInt(orderService.getOrderData(yesterday, null, "order_total").toString());

        // 新增用户数
        Integer userTotalT = userService.getUserData(today, null, "user_total");
        Integer userTotalY = userService.getUserData(yesterday, null, "user_total");

        // 下单用户数
        Integer payOrderUserTotalT = orderRankingService.getPayOrderUserTotal(today);
        Integer payOrderUserTotalY = orderRankingService.getPayOrderUserTotal(yesterday);

        // 最近七天日期订单交易数
        Integer orderTotal7 = Integer.parseInt(orderService.getOrderData(lately7days, today, "order_total").toString());
        //最近七天订单交易金额
        BigDecimal orderTotalPrice7 = orderService.getOrderData(lately7days, today, "order_total_price");
        //订单
        //待处理订单
        Integer reviewOrderTotal = orderService.getReviewOrderTotal();
        //获取售后订单总量
        Integer refundTotal = orderRefundService.getRefundTotal();
        //获取提现订单总量
        Integer cardOrderTotal = orderService.getCardOrderTotal();
        //未审核评论
        Integer reviewCommentTotal = productCommentService.getReviewCommentTotal();
        //库存告急商品
        Integer productStockTotal = productRankingService.getProductStockTotal();

        JSONObject result = new JSONObject();
        result.put("productTotal", productTotal);
        result.put("userTotal", userTotal);
        result.put("orderTotal", orderTotal);
        result.put("productCommentTotal", productCommentTotal);

        result.put("orderTotalPriceT", orderTotalPriceT);
        result.put("orderTotalPriceY", orderTotalPriceY);

        result.put("orderTotalT", orderTotalT);
        result.put("orderTotalY", orderTotalY);
        result.put("userTotalT", userTotalT);
        result.put("userTotalY", userTotalY);
        result.put("payOrderUserTotalT", payOrderUserTotalT);
        result.put("payOrderUserTotalY", payOrderUserTotalY);
        result.put("orderTotal7", orderTotal7);
        result.put("orderTotalPrice7", orderTotalPrice7);
        result.put("reviewOrderTotal", reviewOrderTotal);
        result.put("refundTotal", refundTotal);
        result.put("cardOrderTotal", cardOrderTotal);
        result.put("reviewCommentTotal", reviewCommentTotal);
        result.put("productStockTotal", productStockTotal);

        return result;

    }
}
