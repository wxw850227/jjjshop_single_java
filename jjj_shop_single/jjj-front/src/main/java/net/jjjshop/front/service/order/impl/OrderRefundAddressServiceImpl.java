package net.jjjshop.front.service.order.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.OrderRefundAddress;
import net.jjjshop.common.mapper.order.OrderRefundAddressMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.front.service.order.OrderRefundAddressService;
import net.jjjshop.front.vo.order.OrderRefundAddressVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 售后单退货地址记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-04
 */
@Slf4j
@Service
public class OrderRefundAddressServiceImpl extends BaseServiceImpl<OrderRefundAddressMapper, OrderRefundAddress> implements OrderRefundAddressService {

    /**
     * 获取售后单退货地址
     * @param orderRefundId
     * @return
     */
    public OrderRefundAddressVo getRefundAddress(Integer orderRefundId){
        OrderRefundAddress one = this.getOne(new LambdaQueryWrapper<OrderRefundAddress>().eq(OrderRefundAddress::getOrderRefundId, orderRefundId));
        if(one == null){
            return null;
        }
        OrderRefundAddressVo vo = new OrderRefundAddressVo();
        BeanUtils.copyProperties(one, vo);
        return vo;
    }
}
