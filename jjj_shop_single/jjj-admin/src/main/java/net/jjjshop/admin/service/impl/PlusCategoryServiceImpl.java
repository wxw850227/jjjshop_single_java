package net.jjjshop.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.admin.service.PlusCategoryService;
import net.jjjshop.admin.service.ShopAccessService;
import net.jjjshop.admin.vo.PlusCategoryVo;
import net.jjjshop.common.entity.plus.PlusCategory;
import net.jjjshop.common.entity.shop.ShopAccess;
import net.jjjshop.common.mapper.plus.PlusCategoryMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 插件分类表 服务实现类
 *
 * @author jjjshop
 * @since 2022-06-22
 */
@Slf4j
@Service
public class PlusCategoryServiceImpl extends BaseServiceImpl<PlusCategoryMapper, PlusCategory> implements PlusCategoryService {
    @Autowired
    private PlusCategoryMapper plusCategoryMapper;
    @Autowired
    private ShopAccessService shopAccessService;

    public List<PlusCategoryVo> getAll(){
        LambdaQueryWrapper<PlusCategory> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByAsc(PlusCategory::getSort).orderByAsc(PlusCategory::getCreateTime);
        // 获取所有已启用的权限列表
        List<PlusCategory> list = plusCategoryMapper.selectList(wrapper);
        return list.stream().map(e -> {
            PlusCategoryVo plusCategoryVo = new PlusCategoryVo();
            BeanUtils.copyProperties(e, plusCategoryVo);
            // 子插件
            plusCategoryVo.setChildren(getShopAccessById(e.getPlusCategoryId()));
            return plusCategoryVo;
        }).collect(Collectors.toList());
    }

    /**
     * 根据父分类查找子插件
     * @param categoryId
     * @return
     */
    private List<ShopAccess> getShopAccessById(Integer categoryId){
        LambdaQueryWrapper<ShopAccess> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ShopAccess::getPlusCategoryId, categoryId);
        wrapper.eq(ShopAccess::getIsShow, 1);
        wrapper.orderByAsc(ShopAccess::getSort).orderByAsc(ShopAccess::getCreateTime);
        return shopAccessService.list(wrapper);
    }

}
