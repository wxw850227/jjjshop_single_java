package net.jjjshop.shop.service.settings.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.entity.settings.Delivery;
import net.jjjshop.common.entity.settings.DeliveryRule;
import net.jjjshop.common.mapper.settings.DeliveryMapper;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.setting.DeliveryPageParam;
import net.jjjshop.shop.param.setting.DeliveryParam;
import net.jjjshop.shop.service.product.ProductService;
import net.jjjshop.shop.service.settings.DeliveryRuleService;
import net.jjjshop.shop.service.settings.DeliveryService;
import net.jjjshop.shop.vo.setting.DeliveryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 配送模板主表 服务实现类
 * @author jjjshop
 * @since 2022-06-29
 */
@Slf4j
@Service
public class DeliveryServiceImpl extends BaseServiceImpl<DeliveryMapper, Delivery> implements DeliveryService {

    @Autowired
    private DeliveryRuleService deliveryRuleService;
    @Autowired
    private ProductService productService;

    /**
     * 分页查询
     * @param deliveryPageParam
     * @return
     */
    public Paging<DeliveryVo> getList(DeliveryPageParam deliveryPageParam){
        Page<Delivery> page = new PageInfo<>(deliveryPageParam);
        IPage<Delivery> iPage = this.page(page, new LambdaQueryWrapper<Delivery>().orderByAsc(Delivery::getSort));
        // 最终返回分页对象
        IPage<DeliveryVo> resultPage = iPage.convert(item -> {
            DeliveryVo vo = new DeliveryVo();
            BeanUtil.copyProperties(item, vo);
            vo.setRule(deliveryRuleService.list(new LambdaUpdateWrapper<DeliveryRule>().eq(DeliveryRule::getDeliveryId, vo.getDeliveryId())));
            return vo;
        });
        return new Paging(resultPage);
    }

    /**
     * 新增
     * @param deliveryParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(DeliveryParam deliveryParam){
        // 保存
        Delivery delivery = new Delivery();
        BeanUtils.copyProperties(deliveryParam, delivery);
        this.save(delivery);
        // 保存规则
        this.saveRule(deliveryParam, delivery);
        return true;
    }

    /**
     * 获取详情
     * @param deliveryId
     * @return
     */
    public DeliveryVo getDetail(Integer deliveryId){
        Delivery delivery = this.getById(deliveryId);
        DeliveryVo vo = new DeliveryVo();
        BeanUtils.copyProperties(delivery, vo);
        vo.setRule(deliveryRuleService.list(new LambdaUpdateWrapper<DeliveryRule>().eq(DeliveryRule::getDeliveryId, deliveryId)));
        return vo;
    }

    /**
     * 修改
     * @param deliveryParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean edit(DeliveryParam deliveryParam){
        // 保存
        Delivery delivery = new Delivery();
        BeanUtils.copyProperties(deliveryParam, delivery);
        this.updateById(delivery);
        // 删除规则
        deliveryRuleService.remove(new LambdaUpdateWrapper<DeliveryRule>().eq(DeliveryRule::getDeliveryId, delivery.getDeliveryId()));
        // 保存规则
        this.saveRule(deliveryParam, delivery);
        return true;
    }

    /**
     * 保存规则
     * @param deliveryParam
     * @param delivery
     * @param delivery
     */
    private void saveRule(DeliveryParam deliveryParam, Delivery delivery){
        List<DeliveryRule> rules = new ArrayList<>();
        deliveryParam.getRule().forEach(item -> {
            DeliveryRule rule = new DeliveryRule();
            BeanUtils.copyProperties(item, rule);
            rule.setDeliveryId(delivery.getDeliveryId());
            rules.add(rule);
        });
        deliveryRuleService.saveBatch(rules);
    }

    /**
     * 删除
     * @param deliveryId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean delById(Integer deliveryId){
        int count = productService.count(new LambdaUpdateWrapper<Product>().eq(Product::getDeliveryId, deliveryId));
        if(count > 0){
            throw new BusinessException("该模板被" + count +"个商品使用，不允许删除");
        }
        // 删除规则
        deliveryRuleService.remove(new LambdaUpdateWrapper<DeliveryRule>().eq(DeliveryRule::getDeliveryId, deliveryId));
        return this.removeById(deliveryId);
    }
}
