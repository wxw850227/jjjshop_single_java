package net.jjjshop.shop.service.shop.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.shop.ShopOptLog;
import net.jjjshop.common.entity.shop.ShopUser;
import net.jjjshop.common.mapper.shop.ShopOptLogMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.shop.OptLogPageParam;
import net.jjjshop.shop.service.shop.ShopOptLogService;
import net.jjjshop.shop.service.shop.ShopUserService;
import net.jjjshop.shop.vo.shop.OptLogVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 管理员操作记录表 服务实现类
 * @author jjjshop
 * @since 2022-08-15
 */
@Slf4j
@Service
public class ShopOptLogServiceImpl extends BaseServiceImpl<ShopOptLogMapper, ShopOptLog> implements ShopOptLogService {

    @Autowired
    private ShopUserService shopUserService;

    /**
     * 获取所有操作记录
     * @param optLogPageParam
     * @return
     */
    public Paging<OptLogVo> getList(OptLogPageParam optLogPageParam){
        Page page = new PageInfo(optLogPageParam);
        LambdaQueryWrapper<ShopOptLog> wrapper = new LambdaQueryWrapper<ShopOptLog>();
        if(StringUtils.isNotEmpty(optLogPageParam.getUsername())){
            ShopUser shopUser = shopUserService.getOne(new LambdaQueryWrapper<ShopUser>().eq(ShopUser::getUserName, optLogPageParam.getUsername()));
            wrapper.eq(ShopOptLog::getShopUserId, shopUser.getShopUserId());
        }
        wrapper.orderByDesc(ShopOptLog::getCreateTime);
        IPage iPage = this.page(page, wrapper);
        IPage result = iPage.convert(item -> {
            OptLogVo vo = new OptLogVo();
            BeanUtils.copyProperties(item, vo);
            ShopUser shopUser = shopUserService.getById(vo.getShopUserId());
            vo.setRealName(shopUser.getRealName());
            vo.setUserName(shopUser.getUserName());
            return vo;
        });
        return new Paging(result);
    }
}
