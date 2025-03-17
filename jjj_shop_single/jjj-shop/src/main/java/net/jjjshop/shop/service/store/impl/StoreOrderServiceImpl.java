package net.jjjshop.shop.service.store.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.store.StoreClerk;
import net.jjjshop.common.entity.store.StoreOrder;
import net.jjjshop.common.mapper.store.StoreOrderMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.store.StoreOrderPageParam;
import net.jjjshop.shop.service.order.OrderService;
import net.jjjshop.shop.service.store.StoreClerkService;
import net.jjjshop.shop.service.store.StoreOrderService;
import net.jjjshop.shop.service.store.StoreService;
import net.jjjshop.shop.vo.store.StoreOrderVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商家门店核销订单记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-27
 */
@Slf4j
@Service
public class StoreOrderServiceImpl extends BaseServiceImpl<StoreOrderMapper, StoreOrder> implements StoreOrderService {

    @Autowired
    private StoreClerkService storeClerkService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private OrderService orderService;

    /**
     * 核销记录分页列表
     * @param storeOrderPageParam
     * @return
     */
    public Paging<StoreOrderVo> getList(StoreOrderPageParam storeOrderPageParam) {
        Page page = new PageInfo(storeOrderPageParam);
        LambdaQueryWrapper<StoreOrder> wrapper = new LambdaQueryWrapper<>();
        //获取分页对象携带的查询参数
        wrapper.orderByDesc(StoreOrder::getCreateTime);
        Integer storeId = storeOrderPageParam.getStoreId();
        String search = storeOrderPageParam.getSearch();
        String startDate = storeOrderPageParam.getStartDate();
        String endDate = storeOrderPageParam.getEndDate();
        //查询店名
        if (storeId != null && storeId > 0) {
            wrapper.eq(StoreOrder::getStoreId, storeId);
        }
        //根据店员名称模糊查询
        if (StringUtils.isNotEmpty(search)) {
            List<StoreClerk> clerks = storeClerkService.list(new LambdaQueryWrapper<StoreClerk>().like(StoreClerk::getRealName, search));
            List<Integer> clerkIds = clerks.stream().map(e -> {
                return e.getClerkId();
            }).collect(Collectors.toList());
            wrapper.in(StoreOrder::getClerkId,clerkIds);
        }
        //根据开始时间模糊查询
        if(StringUtils.isNotEmpty(startDate)){
            wrapper.gt(StoreOrder::getCreateTime,startDate);
        }
        //根据结束时间查询
        if(StringUtils.isNotEmpty(endDate)){
            wrapper.lt(StoreOrder::getCreateTime,endDate);
        }
        //获取结果对象
        IPage iPage = this.page(page, wrapper);
        IPage result = iPage.convert(item -> {
            StoreOrderVo vo = new StoreOrderVo();
            BeanUtils.copyProperties(item, vo);
            vo.setStoreName(storeService.getById(vo.getStoreId()).getStoreName());
            vo.setRealName(storeClerkService.getById(vo.getClerkId()).getRealName());
            vo.setOrderNo(orderService.getById(vo.getOrderId()).getOrderNo());
            vo.setOrderTypeText(getOrderTypeAttr(vo.getOrderType()));
            return vo;
        });
        return new Paging(result);

    }

    /**
     * 核销记录订单种类
     * @param type
     * @return
     */
    private String getOrderTypeAttr(Integer type){
        if(type==10){
            return "商城订单";
        }
        if(type==20){
            return "拼团订单";
        }
        if(type==30){
            return "余额充值";
        }
        return "";
    }

    /**
     * 添加订单核销记录
     * @param
     * @return
     */
    public Boolean add(Integer orderId, Integer storeId, Integer clerkId,Integer orderType){
        StoreOrder storeOrder = new StoreOrder();
        storeOrder.setOrderId(orderId);
        storeOrder.setStoreId(storeId);
        storeOrder.setClerkId(clerkId);
        storeOrder.setOrderType(orderType);
        return this.save(storeOrder);
    }


}
