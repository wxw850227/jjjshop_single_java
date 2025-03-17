package net.jjjshop.shop.service.app.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.app.AppUpdate;
import net.jjjshop.common.mapper.app.AppUpdateMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.CommonPageParam;
import net.jjjshop.shop.param.app.AppUpdateParam;
import net.jjjshop.shop.service.app.AppUpdateService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * app升级记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-27
 */
@Slf4j
@Service
public class AppUpdateServiceImpl extends BaseServiceImpl<AppUpdateMapper, AppUpdate> implements AppUpdateService {

    /**
     * 分页查询
     * @param
     * @return
     */
    public Paging<AppUpdate> getList(CommonPageParam commonPageParam) {
        return new Paging<>(this.page(new PageInfo<>(commonPageParam), new LambdaQueryWrapper<AppUpdate>().orderByDesc(AppUpdate::getCreateTime)));
    }

    /**
     * 添加
     * @param appUpdateParam
     * @return
     */
    public boolean add(AppUpdateParam appUpdateParam) {
        AppUpdate bean = new AppUpdate();
        BeanUtils.copyProperties(appUpdateParam, bean);
        return this.save(bean);
    }

    /**
     * 修改
     * @param appUpdateParam
     * @return
     */
    public boolean edit(AppUpdateParam appUpdateParam) {
        AppUpdate bean = new AppUpdate();
        BeanUtils.copyProperties(appUpdateParam, bean);
        return this.updateById(bean);
    }

    /**
     * 真删除
     * @param id
     * @return
     */
    public boolean delById(Integer id) {
        return this.removeById(id);
    }
}
