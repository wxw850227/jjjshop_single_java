package net.jjjshop.shop.service.order.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.order.OrderAddress;
import net.jjjshop.common.entity.order.OrderProduct;
import net.jjjshop.common.entity.store.Store;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.DeliveryTypeEnum;
import net.jjjshop.common.enums.OrderPayTypeEnum;
import net.jjjshop.common.enums.OrderSourceEnum;
import net.jjjshop.common.enums.OrderTypeEnum;
import net.jjjshop.common.factory.product.ProductFactory;
import net.jjjshop.common.mapper.order.OrderMapper;
import net.jjjshop.common.service.settings.RegionService;
import net.jjjshop.common.util.*;
import net.jjjshop.common.vo.order.OrderProductVo;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.order.*;
import net.jjjshop.shop.service.order.ExportService;
import net.jjjshop.shop.service.order.OrderAddressService;
import net.jjjshop.shop.service.order.OrderProductService;
import net.jjjshop.shop.service.order.OrderService;
import net.jjjshop.shop.service.settings.ExpressService;
import net.jjjshop.shop.service.store.StoreClerkService;
import net.jjjshop.shop.service.store.StoreOrderService;
import net.jjjshop.shop.service.store.StoreService;
import net.jjjshop.shop.service.user.UserService;
import net.jjjshop.shop.vo.order.OrderAddressVo;
import net.jjjshop.shop.vo.order.OrderVo;
import net.jjjshop.shop.vo.store.ExtractStoreVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-04
 */
@Slf4j
@Service
public class OrderServiceImpl extends BaseServiceImpl<OrderMapper, Order> implements OrderService {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderProductService orderProductService;
    @Autowired
    private UploadFileUtils uploadFileUtils;
    @Autowired
    private OrderAddressService orderAddressService;
    @Autowired
    private RegionService regionService;
    @Autowired
    private ExpressService expressService;
    @Autowired
    private OrderRefundUtils orderRefundUtils;
    @Autowired
    private ProductFactory productFactory;
    @Autowired
    private OrderUtils orderUtils;
    @Autowired
    private UserCouponUtils userCouponUtils;
    @Autowired
    private UserUtils userUtils;
    @Autowired
    private ExportService exportService;
    @Autowired
    private StoreClerkService storeClerkService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreOrderService storeOrderService;

    /**
     * 订单列表
     * @param orderPageParam
     * @return
     */
    public Paging<OrderVo> getList(OrderPageParam orderPageParam) {
        Page<Order> page = new PageInfo<>(orderPageParam);
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper = this.transWrapper(wrapper, orderPageParam);
        IPage<Order> iPage = this.page(page, wrapper);
        IPage<OrderVo> resultPage = iPage.convert(item -> {
            OrderVo vo = new OrderVo();
            BeanUtil.copyProperties(item, vo);
            vo.setNickname(userService.getById(vo.getUserId()).getNickname());
            vo.setProductList(this.getProductList(vo.getOrderId()));
            vo.setOrderSourceText(OrderSourceEnum.getName(vo.getOrderSource()));
            vo.setOrderStatusText(OrderUtils.getOrderStatusText(item));
            vo.setPayTypeText(OrderPayTypeEnum.getName(vo.getPayType()));
            vo.setDeliveryTypeText(DeliveryTypeEnum.getName(vo.getDeliveryType()));
            return vo;
        });
        return new Paging(resultPage);
    }

    /**
     * 获取所有订单
     * @param orderPageParam
     * @return
     */
    private List<OrderVo> getListAll(OrderPageParam orderPageParam) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper = this.transWrapper(wrapper, orderPageParam);
        List<OrderVo> result = this.list(wrapper).stream().map(e -> {
            OrderVo vo = new OrderVo();
            BeanUtil.copyProperties(e, vo);
            vo.setNickname(userService.getById(vo.getUserId()).getNickname());
            vo.setProductList(this.getProductList(vo.getOrderId()));
            vo.setOrderSourceText(OrderSourceEnum.getName(vo.getOrderSource()));
            vo.setOrderStatusText(OrderUtils.getOrderStatusText(e));
            vo.setPayTypeText(OrderPayTypeEnum.getName(vo.getPayType()));
            vo.setDeliveryTypeText(DeliveryTypeEnum.getName(vo.getDeliveryType()));
            //如果是快递配送
            if (vo.getDeliveryType() == DeliveryTypeEnum.EXPRESS.getValue()) {
                OrderAddressVo orderAddressVo = new OrderAddressVo();
                OrderAddress oa = orderAddressService.getOne(new LambdaQueryWrapper<OrderAddress>().eq(OrderAddress::getOrderId, vo.getOrderId()));
                BeanUtils.copyProperties(oa, orderAddressVo);
                //设置详细收货地址
                JSONObject detailRegion = new JSONObject();
                detailRegion.put("province", regionService.getById(orderAddressVo.getProvinceId()).getName());
                detailRegion.put("city", regionService.getById(orderAddressVo.getCityId()).getName());
                detailRegion.put("region", regionService.getById(orderAddressVo.getRegionId()).getName());
                orderAddressVo.setRegion(detailRegion);
                vo.setAddress(orderAddressVo);
                //如果是自提配送
            } else if (vo.getDeliveryType() == DeliveryTypeEnum.EXTRACT.getValue()) {
                if (vo.getExtractClerkId() != null && vo.getExtractClerkId() > 0) {
                    //如果选择了店员，设置相应的店员信息
                    vo.setExtractClerkName(storeClerkService.getById(vo.getExtractClerkId()).getRealName());
                    vo.setExtractStoreName(storeService.getById(vo.getExtractStoreId()).getStoreName());
                } else if (vo.getExtractStoreId() != null && vo.getExtractStoreId() > 0) {
                    //如果设置了自提门店，设置相应的自提门店信息
                    Store store = storeService.getById(vo.getExtractStoreId());
                    ExtractStoreVo extractStoreVo = new ExtractStoreVo();
                    BeanUtils.copyProperties(store, extractStoreVo);
                    vo.setExtractStore(extractStoreVo);
                    vo.setShopClerkList(storeClerkService.getClerkByStoreId(vo.getExtractStoreId()));
                }
            }
            vo.setExpress(vo.getDeliveryStatus() == 20 ? expressService.getById(vo.getExpressId()) : null);
            vo.setPayStatusText(vo.getPayStatus() == 10 ? "未付款" : "已付款");
            vo.setDeliveryStatusText(vo.getDeliveryStatus() == 10 ? "未发货" : "已发货");
            return vo;
        }).collect(Collectors.toList());
        return result;
    }


    /**
     * 获取订单内所有商品
     * @param orderId
     * @return
     */
    private List<OrderProductVo> getProductList(Integer orderId) {
        List<OrderProduct> list = orderProductService.list(new LambdaQueryWrapper<OrderProduct>()
                .eq(OrderProduct::getOrderId, orderId).orderByAsc(OrderProduct::getOrderProductId));
        return list.stream().map(e -> {
            OrderProductVo vo = new OrderProductVo();
            BeanUtils.copyProperties(e, vo);
            vo.setImagePath(uploadFileUtils.getFilePath(vo.getImageId()));
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 取消订单
     * @param orderCancelParam
     * @return
     */
    public Boolean orderCancel(OrderCancelParam orderCancelParam) {
        Order order = this.getOne(new LambdaQueryWrapper<Order>().eq(Order::getOrderNo, orderCancelParam.getOrderNo()));
        if (order.getDeliveryStatus() == 20 || order.getOrderStatus() != 10 || order.getPayStatus() != 20) {
            throw new BusinessException("订单不允许取消");
        }
        //执行退款操作
        orderRefundUtils.execute(order, order.getPayPrice());
        //回退商品库存
        productFactory.getFactory(order.getOrderSource()).backProductStock(orderUtils.getOrderProduct(order.getOrderId()), true);
        //回退用户优惠券
        if (order.getCouponId() > 0) {
            userCouponUtils.setIsUse(order.getCouponId(), 0);
        }
        // 更新订单状态
        order.setOrderStatus(20);
        order.setCancelRemark(orderCancelParam.getCancelRemark());
        return this.updateById(order);
    }

    /**
     * 审核：用户取消订单
     * @param orderParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean confirmCancel(OrderParam orderParam) {
        Order order = this.getById(orderParam.getOrderId());
        if (order.getOrderStatus() != 21 || order.getPayStatus() != 20) {
            throw new BusinessException("订单不允许取消");
        }
        if (orderParam.getIsCancel()) {
            //执行退款操作
            orderRefundUtils.execute(order, order.getPayPrice());
            //回退商品库存
            productFactory.getFactory(order.getOrderSource()).backProductStock(orderUtils.getOrderProduct(order.getOrderId()), true);
            //回退用户优惠券
            if (order.getCouponId() > 0) {
                userCouponUtils.setIsUse(order.getCouponId(), 0);
            }
        }
        // 更新订单状态
        order.setOrderStatus(orderParam.getIsCancel() ? 20 : 10);
        return this.updateById(order);
    }

    /**
     * 单个订单信息
     * @param orderId
     * @return
     */
    public OrderVo detail(Integer orderId) {
        Order order = this.getById(orderId);
        OrderVo vo = new OrderVo();
        BeanUtils.copyProperties(order, vo);
        User user = userService.getById(vo.getUserId());
        //设置昵称
        vo.setNickname(user.getNickname());
        vo.setMobile(user.getMobile());
        vo.setProductList(this.getProductList(vo.getOrderId()));
        vo.setOrderSourceText(OrderSourceEnum.getName(vo.getOrderSource()));
        vo.setOrderStatusText(OrderUtils.getOrderStatusText(order));
        vo.setPayTypeText(OrderPayTypeEnum.getName(vo.getPayType()));
        vo.setDeliveryTypeText(DeliveryTypeEnum.getName(vo.getDeliveryType()));
        //10未付款 20已付款
        vo.setPayStatusText(vo.getPayStatus() == 10 ? "未付款" : "已付款");
        //10未收货 20已收货
        vo.setDeliveryStatusText(vo.getDeliveryStatus() == 10 ? "未发货" : "已发货");
        //10未收货 20已收货
        vo.setReceiptStatusText(vo.getReceiptStatus() == 10 ? "未收货" : "已收货");
        //设置后台修改价格
        if (vo.getUpdatePrice().compareTo(BigDecimal.ZERO) > 0) {
            vo.setUpdatePriceSymbol("+");
        } else if (vo.getUpdatePrice().compareTo(BigDecimal.ZERO) < 0) {
            vo.setUpdatePriceSymbol("-");
        } else {
            vo.setUpdatePriceSymbol("");
        }
        if (vo.getDeliveryType() == DeliveryTypeEnum.EXPRESS.getValue()) {
            OrderAddressVo orderAddressVo = new OrderAddressVo();
            OrderAddress oa = orderAddressService.getOne(new LambdaQueryWrapper<OrderAddress>().eq(OrderAddress::getOrderId, orderId));
            BeanUtils.copyProperties(oa, orderAddressVo);
            //设置详细收货地址
            JSONObject detailRegion = new JSONObject();
            detailRegion.put("province", regionService.getById(orderAddressVo.getProvinceId()).getName());
            detailRegion.put("city", regionService.getById(orderAddressVo.getCityId()).getName());
            detailRegion.put("region", regionService.getById(orderAddressVo.getRegionId()).getName());
            orderAddressVo.setRegion(detailRegion);
            vo.setAddress(orderAddressVo);
        } else if (vo.getDeliveryType() == DeliveryTypeEnum.EXTRACT.getValue()) {
            if (vo.getExtractClerkId() != null && vo.getExtractClerkId() > 0) {
                //如果已经有了自提门店店员信息，直接设置自提门店名称和自提门店店员
                vo.setExtractClerkName(storeClerkService.getById(vo.getExtractClerkId()).getRealName());
                vo.setExtractStoreName(storeService.getById(vo.getExtractStoreId()).getStoreName());
            } else {
                //如果没有则传入门店信息和门店店员列表
                Store store = storeService.getById(vo.getExtractStoreId());
                ExtractStoreVo extractStoreVo = new ExtractStoreVo();
                BeanUtils.copyProperties(store, extractStoreVo);
                vo.setExtractStore(extractStoreVo);
                vo.setShopClerkList(storeClerkService.getClerkByStoreId(vo.getExtractStoreId()));
            }
        }
        if (vo.getDeliveryStatus() == 20) {
            vo.setExpress(expressService.getById(vo.getExpressId()));
        } else {
            vo.setExpressList(expressService.getAll());
        }
        return vo;
    }

    /**
     * 订单发货
     * @param orderParam
     * @return
     */
    public Boolean delivery(OrderParam orderParam) {
        Order order = this.getById(orderParam.getOrderId());
        if (!verifyDelivery(order)) {
            return false;
        }
        order.setExpressId(orderParam.getExpressId());
        order.setExpressNo(orderParam.getExpressNo());
        order.setDeliveryStatus(20);
        order.setDeliveryTime(new Date());
        return this.updateById(order);
    }

    /**
     * 验证订单是否可以发货
     * @param order
     * @return
     */
    public Boolean verifyDelivery(Order order) {
        if (order.getPayStatus() != 20
                || order.getDeliveryType() != DeliveryTypeEnum.EXPRESS.getValue()
                || order.getDeliveryStatus() != 10) {
            throw new BusinessException("订单号" + order.getOrderNo() + "不满足发货条件");
        }
        return true;
    }

    /**
     * 通过订单状态查询订单数量
     * @param dataType
     * @return
     */
    public Integer getCount(String dataType) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper = this.transferDataType(wrapper, dataType);
        wrapper.eq(Order::getIsDelete, 0);
        return this.count(wrapper);
    }


    /**
     * 获取订单统计数据
     * @param startDate
     * @param endDate
     * @param type
     * @return
     */
    //获取订单统计数据
    public BigDecimal getOrderData(String startDate, String endDate, String type) throws ParseException {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        //开始查询时间不为空
        if (StringUtils.isNotEmpty(startDate)) {
            wrapper.ge(Order::getPayTime, DateUtil.parse(startDate + " 00:00:00"));
        }
        //结束查询时间不为空
        if (StringUtils.isNotEmpty(endDate)) {
            wrapper.le(Order::getPayTime, DateUtil.parse(endDate + " 23:59:59"));
        } else if (StringUtils.isNotEmpty(startDate)) {
            //如果结束查询时间为空,开始查询时间不为空，就默认设置时间查询区间为开始时间+1天
            Date date = DateUtil.parse(startDate + " 23:59:59");
            wrapper.le(Order::getPayTime, date);
        }
        wrapper.eq(Order::getIsDelete, 0);
        wrapper.eq(Order::getPayStatus, 20);
        wrapper.ne(Order::getOrderStatus, 20);
        //根据查询模式返回不同的数值
        if ("order_total".equals(type)) {
            //查询订单总数
            return new BigDecimal(this.count(wrapper)).setScale(0, BigDecimal.ROUND_DOWN);
        } else if ("order_total_price".equals(type)) {
            //查询付款订单总额
            List<Order> list = this.list(wrapper);
            BigDecimal result = BigDecimal.ZERO;
            for (Order o : list) {
                result = result.add(o.getPayPrice());
            }
            return result;
        } else if ("order_user_total".equals(type)) {
            //查询付款用户数
            List<Order> orderList = this.list(wrapper);
            List<Integer> idList = orderList.stream().map(Order::getUserId).collect(Collectors.toList());
            return new BigDecimal(new HashSet<>(idList).size()).setScale(0, BigDecimal.ROUND_DOWN);
        }
        return BigDecimal.ZERO.setScale(0, BigDecimal.ROUND_DOWN);
    }

    /**
     * 获取待处理订单总数
     * @param
     * @return
     */
    public Integer getReviewOrderTotal() {
        return this.count(new LambdaQueryWrapper<Order>()
                .eq(Order::getPayStatus, 20)
                .eq(Order::getDeliveryStatus, 10)
                .eq(Order::getOrderStatus, 10));
    }

    /**
     * 获取提现订单总量
     * @param
     * @return
     */
    public Integer getCardOrderTotal() {
        return this.count(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderStatus, 1)
                .eq(Order::getDeliveryStatus, 0)
                .eq(Order::getIsDelete, 0));
    }

    /**
     * 获取不同状态的订单
     * @param wrapper
     * @param dataType
     * @return
     */
    private LambdaQueryWrapper<Order> transferDataType(LambdaQueryWrapper<Order> wrapper, String dataType) {
        switch (dataType) {
            case "all":
                break;
            case "payment":
                wrapper.eq(Order::getPayStatus, 10);
                wrapper.eq(Order::getOrderStatus, 10);
                break;
            case "delivery":
                wrapper.eq(Order::getPayStatus, 20);
                wrapper.eq(Order::getOrderStatus, 10);
                wrapper.eq(Order::getDeliveryStatus, 10);
                break;
            case "received":
                wrapper.eq(Order::getPayStatus, 20);
                wrapper.eq(Order::getOrderStatus, 10);
                wrapper.eq(Order::getDeliveryStatus, 20);
                wrapper.eq(Order::getReceiptStatus, 10);
                break;
            case "comment":
                wrapper.eq(Order::getIsComment, 0);
                wrapper.eq(Order::getOrderStatus, 30);
                break;
            case "six":
                wrapper.eq(Order::getIsComment, 1);
                wrapper.eq(Order::getOrderStatus, 30);
                break;
        }
        return wrapper;
    }

    /**
     * 获取订单查询条件
     * @param wrapper
     * @param orderPageParam
     * @return
     */
    public LambdaQueryWrapper<Order> transWrapper(LambdaQueryWrapper<Order> wrapper, OrderPageParam orderPageParam) {
        wrapper = this.transferDataType(wrapper, orderPageParam.getDataType());
        wrapper.eq(Order::getIsDelete, false);
        wrapper.orderByDesc(Order::getCreateTime);
        //根据订单号查询
        if (StringUtils.isNotEmpty(orderPageParam.getOrderNo())) {
            List<Integer> list = this.list(new LambdaQueryWrapper<Order>().like(Order::getOrderNo, orderPageParam.getOrderNo()))
                    .stream().map(e -> {
                        return e.getOrderId();
                    }).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(list)) {
                wrapper.in(Order::getOrderId, list);
            } else {
                wrapper.eq(Order::getOrderId, -1);
                return wrapper;
            }
        }
        //查询参数：开始时间
        if (StringUtils.isNotEmpty(orderPageParam.getStartDate())) {
            Date startTime = DateUtil.parse(orderPageParam.getStartDate() + " 00:00:00");
            wrapper.ge(Order::getCreateTime, startTime);
        }
        //查询参数：结束时间
        if (StringUtils.isNotEmpty(orderPageParam.getEndDate())) {
            Date endTime = DateUtil.parse(orderPageParam.getEndDate() + " 23:59:59");
            wrapper.le(Order::getCreateTime, endTime);
        }
        //查询参数：订单运送类型
        if (orderPageParam.getDeliveryType() != null && orderPageParam.getDeliveryType() > 0) {
            wrapper.eq(Order::getDeliveryType, orderPageParam.getDeliveryType());
        }
        return wrapper;
    }

    /**
     * 导出订单
     * @param orderPageParam
     * @param httpServletResponse
     * @return
     */
    public void exportList(OrderPageParam orderPageParam, HttpServletResponse httpServletResponse) throws Exception {
        List<OrderVo> orderList = this.getListAll(orderPageParam);
        exportService.orderList(orderList, httpServletResponse);
    }

    /**
     * 确认核销订单
     * @param orderExtractParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean verificationOrder(OrderExtractParam orderExtractParam) {
        Order order = this.getById(orderExtractParam.getOrderId());
        if (order.getPayStatus() != 20
                || order.getDeliveryType() != DeliveryTypeEnum.EXTRACT.getValue()
                || order.getDeliveryStatus() == 20
                || order.getOrderStatus() == 20
                || order.getOrderStatus() == 21
        ) {
            throw new BusinessException("该订单不满足核销条件");
        }
        order.setExtractClerkId(orderExtractParam.getExtractClerkId());
        order.setDeliveryStatus(20);
        order.setDeliveryTime(new Date());
        order.setReceiptStatus(20);
        order.setReceiptTime(new Date());
        order.setOrderStatus(30);
        boolean update = this.updateById(order);
        //新增核销记录
        storeOrderService.add(order.getOrderId(), order.getExtractStoreId(), order.getExtractClerkId(), OrderTypeEnum.MASTER.getValue());
        //完成订单完成后流程
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);
        orderUtils.complete(orderList, order.getAppId());
        return update;
    }

    /**
     * 获取所有运送方式
     * @param
     * @return
     */
    public List<JSONObject> getDeliveryList() {
        return DeliveryTypeEnum.getDeliveryList();
    }


    /**
     * 虚拟商品发货
     * @param orderVirtualParam
     * @return
     */
    @Transactional
    public boolean virtual(OrderVirtualParam orderVirtualParam) {
        Order order = this.getById(orderVirtualParam.getOrderId());
        if(order.getPayStatus().intValue() != 20
                || order.getDeliveryType().intValue() != DeliveryTypeEnum.NO_EXPRESS.getValue()
                || order.getDeliveryStatus().intValue() == 20
                || order.getOrderStatus().intValue() == 20
                || order.getOrderStatus().intValue() == 21){
            throw new BusinessException("该订单不满足发货条件");
        }
        Date now = new Date();
        order.setDeliveryStatus(20);
        order.setDeliveryTime(now);
        order.setReceiptStatus(20);
        order.setReceiptTime(now);
        order.setOrderStatus(30);
        order.setVirtualContent(orderVirtualParam.getVirtualContent());
        this.updateById(order);
        //执行订单完成后操作
        List<Order> orderList = new ArrayList<>();
        orderList.add(order);
        orderUtils.complete(orderList, order.getAppId());
        return true;
    }

    //修改订单价格
    @Override
    public boolean updatePrice(OrderPriceParam orderPriceParam){
        Order order = this.getById(orderPriceParam.getOrderId());
        if(order.getPayStatus() != 10){
            throw new BusinessException("该订单不合法");
        }
        if(order.getOrderSource() != 10){
            throw new BusinessException("该订单不合法");
        }
        // 实际付款金额
        BigDecimal payPrice = orderPriceParam.getUpdatePrice().add(orderPriceParam.getUpdateExpressPrice()).setScale(2);
        if(payPrice.compareTo(BigDecimal.ZERO) <=0 ){
            throw new BusinessException("订单实付款价格不能为0.00元");
        }
        order.setOrderNo(OrderUtils.geneOrderNo(order.getUserId()));// 修改订单号, 否则微信支付提示重复
        order.setPayPrice(payPrice);
        order.setOrderPrice(orderPriceParam.getUpdatePrice());
        order.setUpdatePrice(orderPriceParam.getUpdatePrice().subtract(order.getTotalPrice().subtract(order.getCouponMoney())));
        order.setExpressPrice(orderPriceParam.getUpdateExpressPrice());
        return this.updateById(order);
    }
}
