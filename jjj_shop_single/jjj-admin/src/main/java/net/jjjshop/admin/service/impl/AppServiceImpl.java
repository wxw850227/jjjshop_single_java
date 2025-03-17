package net.jjjshop.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.admin.param.AppPageParam;
import net.jjjshop.admin.param.AppParam;
import net.jjjshop.admin.service.AppService;
import net.jjjshop.admin.service.ShopUserService;
import net.jjjshop.admin.vo.AppVo;
import net.jjjshop.common.entity.app.App;
import net.jjjshop.common.entity.shop.ShopUser;
import net.jjjshop.common.entity.user.UserGrade;
import net.jjjshop.common.mapper.app.AppMapper;
import net.jjjshop.common.service.page.PageService;
import net.jjjshop.common.service.user.UserGradeService;
import net.jjjshop.common.util.PageUtils;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.shiro.util.SaltUtil;
import net.jjjshop.framework.util.PasswordUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 微信小程序记录表 服务实现类
 *
 * @author jjjshop
 * @since 2022-06-23
 */
@Slf4j
@Service
public class AppServiceImpl extends BaseServiceImpl<AppMapper, App> implements AppService {
    @Autowired
    private ShopUserService shopUserService;
    @Autowired
    private PageService pageService;
    @Autowired
    private UserGradeService userGradeService;
    /**
     * app列表
     * @param appPageParam
     * @return
     */
    public Paging<AppVo> getList(AppPageParam appPageParam){
        Page<App> page = new PageInfo<>(appPageParam, OrderItem.asc(getLambdaColumn(App::getCreateTime)));
        IPage<App> iPage = this.page(page, new LambdaUpdateWrapper<App>().eq(App::getIsDelete, false));
        // 最终返回分页对象
        IPage<AppVo> resultPage = iPage.convert(result -> {
            AppVo vo = new AppVo();
            BeanUtil.copyProperties(result, vo);
            vo.setUserName(shopUserService.getOne(new LambdaQueryWrapper<ShopUser>()
                    .eq(ShopUser::getAppId, vo.getAppId()).eq(ShopUser::getIsSuper, true)).getUserName());
            return vo;
        });
        return new Paging(resultPage);
    }

    /**
     * 修改状态
     * @param appId
     * @return
     */
    public Boolean editStatusById(Integer appId){
        App app = this.getById(appId);
        App updateBean = new App();
        updateBean.setAppId(appId);
        updateBean.setIsRecycle(app.getIsRecycle()?false:true);
        return this.updateById(updateBean);
    }

    /**
     * 修改微信服务商支付状态
     * @param appId
     * @return
     */
    public Boolean updateWxStatus(Integer appId){
        App app = this.getById(appId);
        App updateBean = new App();
        updateBean.setAppId(appId);
        return this.updateById(updateBean);
    }

    /**
     * 删除
     * @param appId
     * @return
     */
    public Boolean setDelete(Integer appId){
        App updateBean = new App();
        updateBean.setAppId(appId);
        updateBean.setIsDelete(true);
        return this.updateById(updateBean);
    }

    /**
     * 新增
     * @param appParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(AppParam appParam){
        if(!appParam.getNoExpire() && StringUtils.isEmpty(appParam.getExpireTime())){
            throw new BusinessException("过期时间不能为空");
        }
        // 是否存在用户名
        if(shopUserService.count(new LambdaQueryWrapper<ShopUser>()
                .eq(ShopUser::getUserName, appParam.getUserName())) > 0){
            throw new BusinessException("商家用户名已存在");
        }
        // 保存主表
        App app = new App();
        BeanUtils.copyProperties(appParam, app);
        // 不过期
        if(appParam.getNoExpire()){
            app.setExpireTime(null);
        }else{
            app.setExpireTime(DateUtil.parseDateTime(appParam.getExpireTime() + " 23:59:59"));
        }
        this.save(app);
        // 保存管理员用户表
        ShopUser user = new ShopUser();
        user.setAppId(app.getAppId());
        user.setUserName(appParam.getUserName());
        user.setIsSuper(true);
        String salt = SaltUtil.generateSalt();
        // 密码加密
        user.setSalt(salt);
        user.setPassword(PasswordUtil.encrypt(appParam.getPassword(), salt));
        // 新增应用diy配置
        this.insertPageDefault(app.getAppId());
        // 默认等级
        this.insertGradeDefault(app.getAppId());
        return shopUserService.save(user);
    }

    /**
     * 页面默认数据
     */
    private void insertPageDefault(Integer appId){
        net.jjjshop.common.entity.page.Page page = new net.jjjshop.common.entity.page.Page();
        page.setPageName("首页");
        page.setPageType(10);
        page.setAppId(appId);
        page.setPageData(PageUtils.getDefaultPage().toJSONString());
        pageService.save(page);
    }

    /**
     * 页面默认数据
     */
    private void insertGradeDefault(Integer appId){
        UserGrade grade = new UserGrade();
        grade.setName("普通会员");
        grade.setIsDefault(1);
        grade.setRemark("新用户即为该等级");
        grade.setAppId(appId);
        userGradeService.save(grade);
    }

    /**
     * 修改
     * @param appParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean edit(AppParam appParam){
        // 更新主表
        App app = new App();
        BeanUtils.copyProperties(appParam, app);
        // 不过期
        if(appParam.getNoExpire()){
            app.setExpireTime(null);
        }else{
            app.setExpireTime(DateUtil.parseDateTime(appParam.getExpireTime() + " 23:59:59"));
        }
        this.updateById(app);
        // 更新管理员表
        ShopUser user = shopUserService.getOne(new LambdaQueryWrapper<ShopUser>()
                .eq(ShopUser::getAppId, app.getAppId())
                .eq(ShopUser::getIsSuper, true));
        ShopUser updateUser = new ShopUser();
        if (!StringUtils.isEmpty(appParam.getPassword())) {
            updateUser.setPassword(PasswordUtil.encrypt(appParam.getPassword(), user.getSalt()));
        }
        if(!appParam.getUserName().equals(user.getUserName())){
            if(shopUserService.count(new LambdaQueryWrapper<ShopUser>()
                    .eq(ShopUser::getUserName, appParam.getUserName())) > 0){
                throw new BusinessException("商家用户名已存在");
            }
        }
        updateUser.setShopUserId(user.getShopUserId());
        updateUser.setUserName(appParam.getUserName());
        return shopUserService.updateById(updateUser);
    }
}
