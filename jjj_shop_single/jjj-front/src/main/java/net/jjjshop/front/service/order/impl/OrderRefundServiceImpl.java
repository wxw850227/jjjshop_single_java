package net.jjjshop.front.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.OrderProduct;
import net.jjjshop.common.entity.order.OrderRefund;
import net.jjjshop.common.entity.order.OrderRefundImage;
import net.jjjshop.common.entity.settings.Express;
import net.jjjshop.common.mapper.order.OrderRefundMapper;
import net.jjjshop.common.util.KuaiDi100Utils;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.common.vo.setting.ImageVo;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.front.param.order.OrderRefundDeliveryParam;
import net.jjjshop.front.param.order.OrderRefundPageParam;
import net.jjjshop.front.param.order.OrderRefundParam;
import net.jjjshop.front.service.order.*;
import net.jjjshop.front.service.settings.ExpressService;
import net.jjjshop.front.service.user.UserOrderService;
import net.jjjshop.front.vo.order.*;
import net.jjjshop.front.vo.settings.ExpressDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private OrderRefundImageService orderRefundImageService;
    @Autowired
    private OrderProductService orderProductService;
    @Autowired
    private UploadFileUtils uploadFileUtils;
    @Autowired
    private UserOrderService userOrderService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ExpressService expressService;
    @Autowired
    private OrderRefundAddressService orderRefundAddressService;
    @Autowired
    private KuaiDi100Utils kuaiDi100Utils;


    /**
     * 售后订单列表
     * @param orderRefundPageParam
     * @return
     */
    public Paging<OrderRefundListVo> getList(OrderRefundPageParam orderRefundPageParam) {
        LambdaQueryWrapper<OrderRefund> wrapper = new LambdaQueryWrapper<>();
        if (orderRefundPageParam.getState() > -1) {
            wrapper.eq(OrderRefund::getStatus, orderRefundPageParam.getState());
        }
        wrapper.eq(OrderRefund::getUserId, orderRefundPageParam.getUserId());
        wrapper.orderByDesc(OrderRefund::getCreateTime);
        Page page = new PageInfo(orderRefundPageParam);
        IPage<OrderRefund> iPage = this.page(page, wrapper);
        IPage<OrderRefundListVo> result = iPage.convert(item -> {
            OrderRefundListVo vo = new OrderRefundListVo();
            BeanUtils.copyProperties(item, vo);
            OrderProductVo orderProductVo = new OrderProductVo();
            OrderProduct orderProduct = orderProductService.getById(item.getOrderProductId());
            BeanUtils.copyProperties(orderProduct, orderProductVo);
            orderProductVo.setProductImage(uploadFileUtils.getFilePath(orderProductVo.getImageId()));
            vo.setOrderProduct(orderProductVo);
            vo.setStateText(this.getStateAttr(vo.getStatus()));
            return vo;
        });
        return new Paging(result);
    }


    /**
     * 售后订单详情
     * @param orderRefundId
     * @return
     */
    public OrderRefundDetailVo detail(Integer orderRefundId) {
        OrderRefund orderRefund = this.getById(orderRefundId);
        OrderRefundDetailVo vo = new OrderRefundDetailVo();
        BeanUtils.copyProperties(orderRefund, vo);
        OrderProductVo orderProductVo = new OrderProductVo();
        OrderProduct orderProduct = orderProductService.getById(orderRefund.getOrderProductId());
        BeanUtils.copyProperties(orderProduct, orderProductVo);
        orderProductVo.setProductImage(uploadFileUtils.getFilePath(orderProductVo.getImageId()));
        vo.setOrderProduct(orderProductVo);
        OrderDetailVo orderDetailVo = userOrderService.transOrderDetailVo(orderService.getById(orderRefund.getOrderId()));
        vo.setOrderM(orderDetailVo);
        vo.setAddress(orderRefundAddressService.getRefundAddress(orderRefundId));
        vo.setStateText(this.getStateAttr(vo.getStatus()));
        vo.setTypeText(this.getTypeAttr(vo.getType()));
        vo.setExpressList(expressService.getAll());
        if (StringUtils.isNotEmpty(vo.getExpressId()) && !"0".equals(vo.getExpressId())) {
            vo.setExpressName(expressService.getById(Integer.valueOf(vo.getExpressId())).getExpressName());
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
     * 售后订单发货
     * @param orderRefundDeliveryParam
     * @return
     */
    public Boolean delivery(OrderRefundDeliveryParam orderRefundDeliveryParam) {
        OrderRefund orderRefund = this.getById(orderRefundDeliveryParam.getOrderRefundId());
        if (orderRefund.getIsAgree() != 10 || orderRefund.getIsUserSend() != 0) {
            throw new BusinessException("当前售后单不合法，不允许该操作");
        }
        if (orderRefundDeliveryParam.getExpressId() <= 0) {
            throw new BusinessException("请选择物流公司");
        }
        if (StringUtils.isEmpty(orderRefundDeliveryParam.getExpressNo())) {
            throw new BusinessException("请填写物流单号");
        }
        orderRefund.setExpressId(String.valueOf(orderRefundDeliveryParam.getExpressId()));
        orderRefund.setExpressNo(orderRefundDeliveryParam.getExpressNo());
        orderRefund.setIsUserSend(1);
        orderRefund.setSendTime(new Date());
        return this.updateById(orderRefund);
    }


    private String getStateAttr(Integer value) {
        //售后单状态(0进行中 10已拒绝 20已完成 30已取消)"
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
        //"售后类型(10退货退款 20换货 30退款)"
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
     * 售后订单申请
     * @param orderRefundParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean apply(OrderRefundParam orderRefundParam) {
        OrderProduct orderProduct = orderProductService.getById(orderRefundParam.getOrderProductId());
        if (this.count(new LambdaQueryWrapper<OrderRefund>().eq(OrderRefund::getOrderProductId, orderRefundParam.getOrderProductId())) > 0) {
            throw new BusinessException("该订单商品已经申请过售后,不能重复申请");
        }
        OrderRefund orderRefund = new OrderRefund();
        orderRefund.setOrderProductId(orderProduct.getOrderProductId());
        orderRefund.setOrderId(orderProduct.getOrderId());
        orderRefund.setUserId(orderProduct.getUserId());
        orderRefund.setType(orderRefundParam.getType());
        orderRefund.setApplyDesc(orderRefundParam.getContent());
        boolean save = this.save(orderRefund);
        this.saveRefundImage(orderRefundParam, orderRefund);
        return save;
    }

    /**
     * 保存售后单图片
     * @param orderRefundParam
     * @param orderRefund
     * @return
     */
    private void saveRefundImage(OrderRefundParam orderRefundParam, OrderRefund orderRefund) {
        orderRefundParam.getImages().stream().forEach(e -> {
            OrderRefundImage image = new OrderRefundImage();
            image.setOrderRefundId(orderRefund.getOrderRefundId());
            image.setImageId(e.getFileId());
            orderRefundImageService.save(image);
        });
    }

    /**
     * 获取售后订单申请需要的数据
     * @param orderProductId
     * @return
     */
    public OrderRefundApplyVo toApply(Integer orderProductId) {
        OrderProduct orderProduct = orderProductService.getById(orderProductId);
        OrderRefundApplyVo vo = new OrderRefundApplyVo();
        BeanUtils.copyProperties(orderProduct, vo);
        OrderDetailVo orderDetailVo = userOrderService.transOrderDetailVo(orderService.getById(orderProduct.getOrderId()));
        vo.setOrderM(orderDetailVo);
        vo.setProductImage(uploadFileUtils.getImagePathByProductId(vo.getProductId()));
        return vo;
    }

    /**
     * 获取售后订单换货物流
     * @param orderRefundId
     * @return
     */
    public ExpressDetailVo express(Integer orderRefundId) throws Exception {
        OrderRefund orderRefund = this.getById(orderRefundId);
        Express express = expressService.getById(orderRefund.getSendExpressId());
        ExpressDetailVo vo = new ExpressDetailVo();
        BeanUtils.copyProperties(express, vo);
        vo.setExpressNo(orderRefund.getSendExpressNo());
        vo.setExpress(kuaiDi100Utils.query(express.getExpressCode(), vo.getExpressNo()));
        return vo;
    }

}
