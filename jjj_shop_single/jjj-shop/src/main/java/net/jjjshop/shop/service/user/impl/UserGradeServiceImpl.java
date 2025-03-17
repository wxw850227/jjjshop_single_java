package net.jjjshop.shop.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.entity.user.UserGrade;
import net.jjjshop.common.mapper.user.UserGradeMapper;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.user.UserGradePageParam;
import net.jjjshop.shop.param.user.UserGradeParam;
import net.jjjshop.shop.service.user.UserGradeService;
import net.jjjshop.shop.service.user.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户会员等级表 服务实现类
 * @author jjjshop
 * @since 2022-06-29
 */
@Slf4j
@Service
public class UserGradeServiceImpl extends BaseServiceImpl<UserGradeMapper, UserGrade> implements UserGradeService {

    @Autowired
    private UserService userService;

    /**
     * 分页查询用户等级
     * @param userGradePageParam
     * @return
     */
    public Paging<UserGrade> getList(UserGradePageParam userGradePageParam) {
        return new Paging<>(this.page(new PageInfo<>(userGradePageParam), new LambdaQueryWrapper<UserGrade>()
                .eq(UserGrade::getIsDelete, 0).orderByAsc(UserGrade::getWeight, UserGrade::getCreateTime)));
    }

    /**
     * 获取所有用户等级
     * @param
     * @return
     */
    public List<UserGrade> getAll() {
        return this.list(new LambdaQueryWrapper<UserGrade>()
                .select(UserGrade::getGradeId, UserGrade::getName)
                .eq(UserGrade::getIsDelete, 0)
                .orderByAsc(UserGrade::getWeight, UserGrade::getCreateTime)
        );
    }

    /**
     * 添加用户等级
     * @param userGradeParam
     * @return
     */
    public Boolean add(UserGradeParam userGradeParam) {
        UserGrade userGrade = new UserGrade();
        this.setRemark(userGradeParam);
        BeanUtils.copyProperties(userGradeParam, userGrade);
        return this.save(userGrade);
    }

    /**
     * 修改用户等级
     * @param userGradeParam
     * @return
     */
    public Boolean edit(UserGradeParam userGradeParam) {
        UserGrade userGrade = new UserGrade();
        this.setRemark(userGradeParam);
        BeanUtils.copyProperties(userGradeParam, userGrade);
        return this.updateById(userGrade);
    }

    /**
     * 软删除用户等级
     * @param id
     * @return
     */
    public Boolean setDelete(Integer id) {
        int count = userService.count(new LambdaQueryWrapper<User>()
                .eq(User::getGradeId, id).eq(User::getIsDelete, 0));
        if (count > 0) {
            throw new BusinessException("已经有" + count + "个该等级的用户，不允许删除");
        }
        return this.update(new LambdaUpdateWrapper<UserGrade>()
                .eq(UserGrade::getGradeId, id).set(UserGrade::getIsDelete, 1));
    }

    /**
     * 设置用户等级的remark字段
     * @param userGradeParam
     * @return
     */
    private void setRemark(UserGradeParam userGradeParam) {
        StringBuffer remark = new StringBuffer();
        if (userGradeParam.getOpenMoney() == 1) {
            remark.append("会员消费满" + userGradeParam.getUpgradeMoney() + "元可升级到此等级");
        }
        userGradeParam.setRemark(remark.toString());
    }

}
