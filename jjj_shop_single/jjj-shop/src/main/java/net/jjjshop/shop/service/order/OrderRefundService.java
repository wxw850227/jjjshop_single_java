package net.jjjshop.shop.service.order;


import net.jjjshop.common.entity.order.OrderRefund;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.order.OrderRefundPageParam;
import net.jjjshop.shop.param.order.OrderRefundParam;
import net.jjjshop.shop.vo.order.OrderRefundVo;

import java.math.BigDecimal;
import java.text.ParseException;

/**
 * 售后单记录表 服务类
 * @author jjjshop
 * @since 2022-07-04
 */
public interface OrderRefundService extends BaseService<OrderRefund> {

    /**
     * 获取售后订单分页列表
     * @param orderRefundPageParam
     * @return
     */
    Paging<OrderRefundVo> getList(OrderRefundPageParam orderRefundPageParam);

    /**
     * 获取售后单数据
     * @param orderRefundPageParam
     * @return
     */
    Integer getCountByStatus(OrderRefundPageParam orderRefundPageParam);

    /**
     * 提交售后单
     * @param orderRefundParam
     * @return
     */
    Boolean audit(OrderRefundParam orderRefundParam);

    /**
     * 获取售后订单详情
     * @param orderRefundId
     * @return
     */
    OrderRefundVo detail(Integer orderRefundId);

    /**
     * 按照时间范围获取退款订单数据
     * @param startDate
     * @param endDate
     * @param type
     * @return
     */
    BigDecimal getRefundData(String startDate, String endDate, String type) throws ParseException;

    /**
     * 获取退款订单总数
     * @param
     * @return
     */
    Integer getRefundTotal();

    /**
     * 确认收货退款
     * @param orderRefundParam
     * @return
     */
    Boolean receipt(OrderRefundParam orderRefundParam);
}
