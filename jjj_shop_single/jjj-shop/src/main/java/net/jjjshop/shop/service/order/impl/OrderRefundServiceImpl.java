package net.jjjshop.shop.service.order.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.*;
import net.jjjshop.common.entity.settings.ReturnAddress;
import net.jjjshop.common.mapper.order.OrderRefundMapper;
import net.jjjshop.common.util.OrderRefundUtils;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.common.vo.setting.ImageVo;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.order.OrderRefundPageParam;
import net.jjjshop.shop.param.order.OrderRefundParam;
import net.jjjshop.shop.service.order.*;
import net.jjjshop.shop.service.settings.ExpressService;
import net.jjjshop.shop.service.settings.ReturnAddressService;
import net.jjjshop.shop.service.user.UserService;
import net.jjjshop.shop.vo.order.OrderProductVo;
import net.jjjshop.shop.vo.order.OrderRefundVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 售后单记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-04
 */
@Slf4j
@Service
public class OrderRefundServiceImpl extends BaseServiceImpl<OrderRefundMapper, OrderRefund> implements OrderRefundService {

    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderProductService orderProductService;
    @Autowired
    private UploadFileUtils uploadFileUtils;
    @Autowired
    private ReturnAddressService returnAddressService;
    @Autowired
    private OrderRefundAddressService orderRefundAddressService;
    @Autowired
    private OrderRefundImageService orderRefundImageService;
    @Autowired
    private ExpressService expressService;
    @Autowired
    private OrderRefundUtils orderRefundUtils;

    /**
     * 获取售后订单分页列表
     * @param orderRefundPageParam
     * @return
     */
    public Paging<OrderRefundVo> getList(OrderRefundPageParam orderRefundPageParam) {
        LambdaQueryWrapper<OrderRefund> wrapper = new LambdaQueryWrapper<>();
        //根据订单号模糊搜索
        if (StringUtils.isNotEmpty(orderRefundPageParam.getOrderNo())) {
            List<Integer> list = orderService.list(new LambdaQueryWrapper<Order>().like(Order::getOrderNo, orderRefundPageParam.getOrderNo()))
                    .stream().map(e -> {
                        return e.getOrderId();
                    }).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(list)) {
                wrapper.in(OrderRefund::getOrderId, list);
            }
        }
        //查询参数：开始时间
        if (StringUtils.isNotEmpty(orderRefundPageParam.getStartDate())) {
            Date startTime = DateUtil.parse(orderRefundPageParam.getStartDate() + " 00:00:00");
            wrapper.ge(OrderRefund::getCreateTime, startTime);
        }
        //查询参数：结束时间
        if (StringUtils.isNotEmpty(orderRefundPageParam.getEndDate())) {
            Date endTime = DateUtil.parse(orderRefundPageParam.getEndDate() + " 23:59:59");
            wrapper.le(OrderRefund::getCreateTime, endTime);
        }
        //查询参数：售后单种类
        if (orderRefundPageParam.getType() != null && orderRefundPageParam.getType() > 0) {
            wrapper.eq(OrderRefund::getType, orderRefundPageParam.getType());
        }
        //查询参数：售后单状态
        if (orderRefundPageParam.getStatus() != null && orderRefundPageParam.getStatus() >= 0) {
            wrapper.eq(OrderRefund::getStatus, orderRefundPageParam.getStatus());
        }
        wrapper.orderByDesc(OrderRefund::getCreateTime);
        Page page = new PageInfo(orderRefundPageParam);
        IPage<OrderRefund> iPage = this.page(page, wrapper);
        IPage<OrderRefundVo> result = iPage.convert(item -> {
            OrderRefundVo vo = new OrderRefundVo();
            BeanUtils.copyProperties(item, vo);
            OrderProduct orderProduct = orderProductService.getById(vo.getOrderProductId());
            //设置售后单商品信息
            OrderProductVo orderProductVo = new OrderProductVo();
            BeanUtils.copyProperties(orderProduct, orderProductVo);
            orderProductVo.setImagePath(uploadFileUtils.getFilePath(orderProductVo.getImageId()));
            vo.setOrderProduct(orderProductVo);
            //设置售后单用户信息
            vo.setUser(userService.getById(item.getUserId()));
            //设置售后单订单信息
            Order order = orderService.getById(item.getOrderId());
            vo.setOrderNo(order.getOrderNo());
            vo.setOrderCreateTime(order.getCreateTime());
            vo.setIsAgreeText(this.getIsAgreeAttr(vo.getIsAgree()));
            vo.setStatusText(this.getStateAttr(vo.getStatus()));
            vo.setTypeText(this.getTypeAttr(vo.getType()));
            vo.setOrderCreateTime(order.getCreateTime());
            return vo;
        });
        return new Paging(result);
    }

    private String getIsAgreeAttr(Integer value) {
        switch (value) {
            case 0:
                return "进行中";
            case 10:
                return "已同意";
            case 20:
                return "已拒绝";
        }
        return "";
    }

    private String getStateAttr(Integer value) {
        //售后单状态(0进行中 10已拒绝 20已完成 30已取消)
        switch (value) {
            case 0:
                return "进行中";
            case 10:
                return "已拒绝";
            case 20:
                return "已完成";
            case 30:
                return "已取消";
        }
        return "";
    }


    private String getTypeAttr(Integer value) {
        //售后类型(10退货退款 20换货 30退款)
        switch (value) {
            case 10:
                return "退货退款";
            case 20:
                return "换货";
            case 30:
                return "退款";
        }
        return "";
    }

    /**
     * 获取售后单数据
     * @param orderRefundPageParam
     * @return
     */
    public Integer getCountByStatus(OrderRefundPageParam orderRefundPageParam) {
        LambdaQueryWrapper<OrderRefund> wrapper = new LambdaQueryWrapper<>();
        //根据订单号模糊搜索
        if (StringUtils.isNotEmpty(orderRefundPageParam.getOrderNo())) {
            List<Integer> list = orderService.list(new LambdaQueryWrapper<Order>().like(Order::getOrderNo, orderRefundPageParam.getOrderNo()))
                    .stream().map(e -> {
                        return e.getOrderId();
                    }).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(list)) {
                wrapper.in(OrderRefund::getOrderId, list);
            }
        }
        //查询参数：开始时间
        if (StringUtils.isNotEmpty(orderRefundPageParam.getStartDate())) {
            Date startTime = DateUtil.parse(orderRefundPageParam.getStartDate() + " 00:00:00");
            wrapper.ge(OrderRefund::getCreateTime, startTime);
        }
        //查询参数：结束时间
        if (StringUtils.isNotEmpty(orderRefundPageParam.getEndDate())) {
            Date endTime = DateUtil.parse(orderRefundPageParam.getEndDate() + " 23:59:59");
            wrapper.le(OrderRefund::getCreateTime, endTime);
        }
        //查询参数：售后单种类
        if (orderRefundPageParam.getType() != null && orderRefundPageParam.getType() > 0) {
            wrapper.eq(OrderRefund::getType, orderRefundPageParam.getType());
        }
        wrapper.orderByDesc(OrderRefund::getCreateTime);
        wrapper.eq(OrderRefund::getStatus, orderRefundPageParam.getStatus());
        return this.count(wrapper);
    }

    /**
     * 提交售后单
     * @param orderRefundParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean audit(OrderRefundParam orderRefundParam) {
        OrderRefund orderRefund = this.getById(orderRefundParam.getOrderRefundId());
        if (orderRefundParam.getIsAgree() == 20 && StringUtils.isEmpty(orderRefundParam.getRefuseDesc())) {
            throw new BusinessException("请输入拒绝原因");
        } else if (orderRefundParam.getIsAgree() == 20 && StringUtils.isNotEmpty(orderRefundParam.getRefuseDesc())) {
            orderRefund.setStatus(10);
            orderRefund.setRefuseDesc(orderRefundParam.getRefuseDesc());
        }
        if (orderRefundParam.getIsAgree() == 10 && orderRefund.getType() != 30 && orderRefundParam.getAddressId() == null) {
            throw new BusinessException("请选择收货地址");
        }
        orderRefund.setIsAgree(orderRefundParam.getIsAgree());
        //如果同意退货设置退货地址
        if (orderRefundParam.getIsAgree() == 10 && orderRefund.getType() != 30) {
            this.updateById(orderRefund);
            ReturnAddress returnAddress = returnAddressService.getById(orderRefundParam.getAddressId());
            OrderRefundAddress orderRefundAddress = new OrderRefundAddress();
            orderRefundAddress.setOrderRefundId(orderRefundParam.getOrderRefundId());
            orderRefundAddress.setName(returnAddress.getName());
            orderRefundAddress.setPhone(returnAddress.getPhone());
            orderRefundAddress.setDetail(returnAddress.getDetail());
            orderRefundAddressService.save(orderRefundAddress);
        }
        //如果是仅退款
        if (orderRefundParam.getIsAgree() == 10 && orderRefund.getType() == 30) {
            OrderProduct orderProduct = orderProductService.getById(orderRefund.getOrderProductId());
            BigDecimal totalPayPrice = orderProduct.getTotalPayPrice();
            if (totalPayPrice.compareTo(orderRefundParam.getRefundMoney()) < 0) {
                throw new BusinessException("退款金额不能大于实际支付金额");
            }
            Order order = orderService.getById(orderRefund.getOrderId());
            this.refundMoney(order, orderRefundParam);
        }
        return this.updateById(orderRefund);
    }

    /**
     * 确认收货退款
     * @param orderRefundParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean receipt(OrderRefundParam orderRefundParam) {
        OrderRefund orderRefund = this.getById(orderRefundParam.getOrderRefundId());
        OrderProduct orderProduct = orderProductService.getById(orderRefund.getOrderProductId());
        BigDecimal totalPayPrice = orderProduct.getTotalPayPrice();
        if (totalPayPrice.compareTo(orderRefundParam.getRefundMoney()) < 0) {
            throw new BusinessException("退款金额不能大于实际支付金额");
        }
        Order order = orderService.getById(orderRefund.getOrderId());
        this.refundMoney(order, orderRefundParam);
        return true;
    }

    /**
     * 退款
     * @param order
     * @param orderRefundParam
     * @return
     */
    private void refundMoney(Order order, OrderRefundParam orderRefundParam) {
        OrderRefund orderRefund = this.getById(orderRefundParam);
        orderRefund.setIsReceipt(1);
        orderRefund.setStatus(20);
        if (orderRefund.getType() == 20) {
            orderRefund.setSendExpressId(String.valueOf(orderRefundParam.getSendExpressId()));
            orderRefund.setSendExpressNo(orderRefundParam.getSendExpressNo());
            orderRefund.setDeliverTime(new Date());
            orderRefund.setIsPlateSend(1);
        }
        if (orderRefundParam.getRefundMoney().compareTo(BigDecimal.ZERO) > 0) {
            orderRefund.setRefundMoney(orderRefundParam.getRefundMoney());
        }
        this.updateById(orderRefund);
        if (order.getIsSettled() == 1) {
            //消减用户的消费金额
            userService.setDecUserExpend(userService.getById(order.getUserId()), orderRefundParam.getRefundMoney());
        }
        if (orderRefundParam.getRefundMoney().compareTo(BigDecimal.ZERO) > 0) {
            //执行退款
            orderRefundUtils.execute(order, orderRefundParam.getRefundMoney());
            order.setRefundMoney(order.getRefundMoney().add(orderRefundParam.getRefundMoney()));
            orderService.updateById(order);
        }
        //退预售定金
        //发送模板消息
    }

    /**
     * 获取售后订单详情
     * @param orderRefundId
     * @return
     */
    public OrderRefundVo detail(Integer orderRefundId) {
        OrderRefund orderRefund = this.getById(orderRefundId);
        OrderRefundVo vo = new OrderRefundVo();
        BeanUtils.copyProperties(orderRefund, vo);
        OrderProduct orderProduct = orderProductService.getById(vo.getOrderProductId());
        OrderProductVo orderProductVo = new OrderProductVo();
        BeanUtils.copyProperties(orderProduct, orderProductVo);
        orderProductVo.setImagePath(uploadFileUtils.getFilePath(orderProductVo.getProductId()));
        vo.setOrderProduct(orderProductVo);
        vo.setUser(userService.getById(orderRefund.getUserId()));
        Order order = orderService.getById(orderRefund.getOrderId());
        vo.setOrder(order);
        vo.setIsAgreeText(this.getIsAgreeAttr(vo.getIsAgree()));
        vo.setStatusText(this.getStateAttr(vo.getStatus()));
        vo.setTypeText(this.getTypeAttr(vo.getType()));
        if (vo.getIsAgree() == 0) {
            vo.setAddressList(returnAddressService.getAll());
        } else if (vo.getIsAgree() == 10) {
            vo.setAddress(orderRefundAddressService.getOne(new LambdaQueryWrapper<OrderRefundAddress>().eq(OrderRefundAddress::getOrderRefundId, vo.getOrderRefundId())));
        }
        if (vo.getIsUserSend() == 1) {
            vo.setExpress(expressService.getById(vo.getExpressId()));
        }
        if (vo.getType() == 20 && vo.getStatus() == 0) {
            vo.setExpressList(expressService.getAll());
        }
        if (vo.getIsPlateSend() == 1) {
            vo.setSendExpressName(expressService.getById(vo.getSendExpressId()).getExpressName());
        }
        List<OrderRefundImage> refundImages = orderRefundImageService.list(new LambdaQueryWrapper<OrderRefundImage>()
                .eq(OrderRefundImage::getOrderRefundId, vo.getOrderRefundId()));
        List<ImageVo> images = refundImages.stream().map(e -> {
            ImageVo image = new ImageVo();
            image.setFileId(e.getImageId());
            image.setFilePath(uploadFileUtils.getFilePath(e.getImageId()));
            return image;
        }).collect(Collectors.toList());
        vo.setImages(images);
        return vo;


    }

    /**
     * 按照时间范围获取退款订单数据
     * @param startDate
     * @param endDate
     * @param type
     * @return
     */
    public BigDecimal getRefundData(String startDate, String endDate, String type) {
        LambdaQueryWrapper<OrderRefund> wrapper = new LambdaQueryWrapper<>();
        Date startTime = DateUtil.parse(startDate + " 00:00:00");
        if (StringUtils.isNotEmpty(startDate)) {
            wrapper.ge(OrderRefund::getCreateTime, DateUtil.parse(startDate + " 00:00:00"));
        }
        if (StringUtils.isNotEmpty(endDate)) {
            wrapper.le(OrderRefund::getCreateTime, DateUtil.parse(endDate + " 23:59:59"));
        } else if (StringUtils.isNotEmpty(startDate)) {
            wrapper.le(OrderRefund::getCreateTime, DateUtil.parse(startDate + " 23:59:59"));
        }
        wrapper.eq(OrderRefund::getIsAgree, 10);
        //获取退款金额
        if ("order_refund_money".equals(type)) {
            List<OrderRefund> list = this.list(wrapper);
            BigDecimal result = BigDecimal.ZERO;
            for (OrderRefund e : list) {
                result = result.add(e.getRefundMoney());
            }
            return result;
        } else if ("order_refund_total".equals(type)) {
            //获取退款订单数
            return new BigDecimal(this.count(wrapper));
        }
        return BigDecimal.ZERO;
    }

    /**
     * 获取退款订单总数
     * @param
     * @return
     */
    public Integer getRefundTotal() {
        return this.count(new LambdaQueryWrapper<OrderRefund>().eq(OrderRefund::getIsAgree, 0));
    }

}
