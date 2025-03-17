package net.jjjshop.front.service.order;

import net.jjjshop.common.entity.order.OrderRefund;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.front.param.order.OrderRefundDeliveryParam;
import net.jjjshop.front.param.order.OrderRefundPageParam;
import net.jjjshop.front.param.order.OrderRefundParam;
import net.jjjshop.front.vo.order.OrderRefundApplyVo;
import net.jjjshop.front.vo.order.OrderRefundDetailVo;
import net.jjjshop.front.vo.order.OrderRefundListVo;
import net.jjjshop.front.vo.settings.ExpressDetailVo;

/**
 * 售后单记录表 服务类
 *
 * @author jjjshop
 * @since 2022-07-04
 */
public interface OrderRefundService extends BaseService<OrderRefund> {

    /**
     * 售后订单列表
     * @param orderRefundPageParam
     * @return
     */
    Paging<OrderRefundListVo> getList(OrderRefundPageParam orderRefundPageParam);

    /**
     * 售后订单申请
     * @param orderRefundParam
     * @return
     */
    Boolean apply(OrderRefundParam orderRefundParam);

    /**
     * 售后订单详情
     * @param orderRefundId
     * @return
     */
    OrderRefundDetailVo detail(Integer orderRefundId);

    /**
     * 获取售后订单申请需要的数据
     * @param orderProductId
     * @return
     */
    OrderRefundApplyVo toApply(Integer orderProductId);

    /**
     * 售后订单发货
     * @param orderRefundDeliveryParam
     * @return
     */
    Boolean delivery(OrderRefundDeliveryParam orderRefundDeliveryParam);

    /**
     * 获取售后订单换货物流
     * @param orderRefundId
     * @return
     */
    ExpressDetailVo express(Integer orderRefundId) throws Exception;
}
