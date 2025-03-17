package net.jjjshop.shop.service.plus.plus.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.plus.PlusCategory;
import net.jjjshop.common.entity.shop.ShopAccess;
import net.jjjshop.common.mapper.plus.PlusCategoryMapper;
import net.jjjshop.config.constant.CommonConstant;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.shop.service.plus.plus.PlusCategoryService;
import net.jjjshop.shop.service.shop.ShopAccessService;
import net.jjjshop.shop.vo.plus.PlusCategoryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 插件分类表 服务实现类
 * @author jjjshop
 * @since 2022-07-23
 */
@Slf4j
@Service
public class PlusCategoryServiceImpl extends BaseServiceImpl<PlusCategoryMapper, PlusCategory> implements PlusCategoryService {

    @Autowired
    private ShopAccessService shopAccessService;

    /**
     * 插件列表
     * @param
     * @return
     */
    public List<PlusCategoryVo> getAll() {
        List<PlusCategory> list = this.list(new LambdaQueryWrapper<PlusCategory>()
                .orderByAsc(PlusCategory::getSort, PlusCategory::getCreateTime)
                .comment(CommonConstant.NOT_WITH_App_Id));
        return list.stream().map(e -> {
            List<ShopAccess> children = shopAccessService.list(new LambdaQueryWrapper<ShopAccess>()
                    .eq(ShopAccess::getPlusCategoryId, e.getPlusCategoryId())
                    .comment(CommonConstant.NOT_WITH_App_Id));
            PlusCategoryVo vo = new PlusCategoryVo();
            BeanUtils.copyProperties(e, vo);
            vo.setChildren(children);
            return vo;
        }).collect(Collectors.toList());
    }

}
