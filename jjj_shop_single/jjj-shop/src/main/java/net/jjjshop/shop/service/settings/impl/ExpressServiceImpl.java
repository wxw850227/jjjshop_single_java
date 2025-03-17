package net.jjjshop.shop.service.settings.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.order.Order;
import net.jjjshop.common.entity.settings.Express;
import net.jjjshop.common.mapper.settings.ExpressMapper;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.setting.ExpressPageParam;
import net.jjjshop.shop.param.setting.ExpressParam;
import net.jjjshop.shop.service.order.OrderService;
import net.jjjshop.shop.service.settings.ExpressService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 物流公司记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-19
 */
@Slf4j
@Service
public class ExpressServiceImpl extends BaseServiceImpl<ExpressMapper, Express> implements ExpressService {

    @Autowired
    private OrderService orderService;

    /**
     * 物流公司分页查询
     * @param expressPageParam
     * @return
     */
    public Paging<Express> getList(ExpressPageParam expressPageParam) {
        return new Paging<>(this.page(new PageInfo<>(expressPageParam), new LambdaQueryWrapper<Express>().orderByAsc(Express::getSort)));
    }

    /**
     * 获取所有物流公司
     * @param
     * @return
     */
    public List<Express> getAll(){
        return this.list(new LambdaQueryWrapper<Express>().orderByAsc(Express::getSort));
    }

    /**
     * 修改物流公司
     * @param expressParam
     * @return
     */
    public boolean edit(ExpressParam expressParam) {
        Express express = new Express();
        BeanUtils.copyProperties(expressParam, express);
        return this.updateById(express);
    }

    /**
     * 添加物流公司
     * @param expressParam
     * @return
     */
    public boolean add(ExpressParam expressParam) {
        Express express = new Express();
        BeanUtils.copyProperties(expressParam, express);
        return this.save(express);
    }

    /**
     * 真删除物流公司
     * @param id
     * @return
     */
    public boolean delById(Integer id) {
        Integer count = orderService.count(new LambdaQueryWrapper<Order>().eq(Order::getExpressId, id));
        if (count.intValue() > 0) {
            throw new BusinessException("已经有" + count + "个订单在使用该物流公司，不允许删除");
        }
        return this.removeById(id);
    }
}
