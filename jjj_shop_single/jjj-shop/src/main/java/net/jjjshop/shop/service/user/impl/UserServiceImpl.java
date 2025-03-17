package net.jjjshop.shop.service.user.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.*;
import net.jjjshop.common.mapper.user.UserMapper;
import net.jjjshop.common.util.UserUtils;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.user.UserPageParam;
import net.jjjshop.shop.param.user.UserParam;
import net.jjjshop.shop.service.user.*;
import net.jjjshop.shop.vo.user.UserVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-01
 */
@Slf4j
@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserGradeService userGradeService;
    @Autowired
    private UserGradeLogService userGradeLogService;
    @Autowired
    private UserBalanceLogService userBalanceLogService;
    @Autowired
    private TagService tagService;
    @Autowired
    private UserTagService userTagService;
    @Autowired
    private UserUtils userUtils;

    /**
     * 查找用户
     * @param userPageParam
     * @return
     */
    public Paging<UserVo> getList(UserPageParam userPageParam) {
        // 用户列表
        Page<User> page = new PageInfo<>(userPageParam);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getIsDelete, 0);
        if(StringUtils.isNotEmpty(userPageParam.getSearch())){
            wrapper.like(User::getNickname, userPageParam.getSearch());
        }
        if(StringUtils.isNotEmpty(userPageParam.getNickName())){
            wrapper.and(i->i.or().like(User::getNickname, userPageParam.getNickName())
                    .or().like(User::getMobile, userPageParam.getNickName()));
        }
        if(userPageParam.getGradeId() != null && userPageParam.getGradeId() != 0){
            wrapper.eq(User::getGradeId, userPageParam.getGradeId());
        }
        if(userPageParam.getSex() != null && userPageParam.getSex() != -1){
            wrapper.eq(User::getGender,userPageParam.getSex());
        }
        if(userPageParam.getStartDate() != null){
            wrapper.ge(User::getCreateTime,  DateUtil.format(userPageParam.getStartDate(), "yyyy-MM-dd 00:00:00"));
        }
        if(userPageParam.getEndDate() != null){
            wrapper.le(User::getCreateTime,DateUtil.format(userPageParam.getEndDate(), "yyyy-MM-dd 23:59:59"));
        }else if(userPageParam.getStartDate() != null){
            wrapper.le(User::getCreateTime,  DateUtil.format(userPageParam.getStartDate(), "yyyy-MM-dd 23:59:59"));
        }
        if(userPageParam.getTagId() != null && userPageParam.getTagId() != 0){
            List<Integer> list = userTagService.list(new LambdaQueryWrapper<UserTag>().eq(UserTag::getTagId, userPageParam.getTagId()))
                    .stream().map(UserTag::getUserId).collect(Collectors.toList());
            HashSet<Integer> set = new HashSet<>(list);
            if(CollectionUtils.isNotEmpty(set)){
                wrapper.in(User::getUserId, set);
            }else {
                wrapper.eq(User::getUserId, -1);
            }
        }
        wrapper.orderByDesc(User::getCreateTime);
        IPage<User> iPage = this.page(page, wrapper);
        // 最终返回分页对象
        IPage<UserVo> resultPage = iPage.convert(item -> {
            UserVo vo = new UserVo();
            BeanUtil.copyProperties(item, vo);
            vo.setGradeName(userGradeService.getById(vo.getGradeId()).getName());
            return vo;
        });
        return new Paging(resultPage);

    }

    /**
     * 获取所有用户
     * @param
     * @return
     */
    public List<UserVo> getAll(){
        List<User> list = this.list(new LambdaQueryWrapper<User>().eq(User::getIsDelete, 0));
        List<UserVo> result = list.stream().map(e -> {
            UserVo vo = new UserVo();
            BeanUtils.copyProperties(e, vo);
            vo.setGradeName(userGradeService.getById(e.getGradeId()).getName());
            return vo;
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * 通过等级Id获取所有用户Id
     * @param
     * @return
     */
    public List<Integer> getUserIdsByGrade(Integer gradeId){
        List<User> list = this.list(new LambdaQueryWrapper<User>().eq(User::getIsDelete, 0).eq(User::getGradeId, gradeId).orderByAsc(User::getUserId));
        List<Integer> result = list.stream().map(e -> {
            return e.getUserId();
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * 获取所有用户Id
     * @param
     * @return
     */
    public List<Integer> getUserIds(){
        List<User> list = this.list(new LambdaQueryWrapper<User>().eq(User::getIsDelete, 0).orderByAsc(User::getUserId));
        List<Integer> result = list.stream().map(e -> {
            return e.getUserId();
        }).collect(Collectors.toList());
        return result;
    }

    /**
     * 修改用户余额
     * @param userParam
     * @return
     */
    public Boolean recharge(UserParam userParam) {
        if (userParam.getSource() == 0) {
            return this.rechargeToBalance(userParam);
        }
        return false;
    }

    /**
     * 修改用户等级
     * @param userParam
     * @return
     */
    public Boolean updateGrade(UserParam userParam) {
        User user = this.getById(userParam.getUserId());
        //设置等级变化日志
        UserGradeLog userGradeLog = new UserGradeLog();
        userGradeLog.setUserId(user.getUserId());
        // 变更前的等级id
        userGradeLog.setOldGradeId(user.getGradeId());
        // 变更后的等级id
        userGradeLog.setNewGradeId(userParam.getGradeId());
        //10表示为管理员修改，20表示系统自动修改
        userGradeLog.setChangeType(10);
        userGradeLog.setRemark(userParam.getRemark());
        //保存等级日志变化
        userGradeLogService.save(userGradeLog);
        //修改用户等级
        user.setGradeId(userParam.getGradeId());
        return this.updateById(user);
    }

    /**
     * 获取用户标签数据
     * @param userId
     * @return
     */
    public Map<String, Object> toEditTag(Integer userId) {
        Map<String, Object> map = new HashMap<>();
        //获取所有标签
        List<Tag> allTag = tagService.getAll();
        //获取用户拥有的标签id的list集合
        List<Integer> userTag = userTagService.list(new LambdaQueryWrapper<UserTag>().eq(UserTag::getUserId, userId)).stream().map(item -> {
            Integer tagId = item.getTagId();
            return tagId;
        }).collect(Collectors.toList());
        map.put("userTag", userTag);
        map.put("allTag", allTag);
        return map;
    }


    /**
     * 修改用户标签
     * @param userParam
     * @return
     */
    public Boolean editTag(UserParam userParam) {
        Integer userId = userParam.getUserId();
        List<Integer> checkedTag = userParam.getCheckedTag();
        // 删除所有标签
        userTagService.remove(new LambdaQueryWrapper<UserTag>().eq(UserTag::getUserId, userParam.getUserId()));
        //将所有用户标签保存
        List<UserTag> list = checkedTag.stream().map(item -> {
            UserTag userTag = new UserTag();
            userTag.setUserId(userId);
            userTag.setTagId(item);
            return userTag;
        }).collect(Collectors.toList());
        return userTagService.saveBatch(list);
    }


    /**
     * 软删除用户
     * @param userId
     * @return
     */
    public Boolean setDelete(Integer userId) {
        // 判断是否为分销商
        // 删除用户推荐关系
        // 软删除
        return this.update(new LambdaUpdateWrapper<User>().eq(User::getUserId, userId).set(User::getIsDelete, 1));
    }

    /**
     * 修改用户余额
     * @param userParam
     * @return
     */
    private Boolean rechargeToBalance(UserParam userParam) {
        User user = this.getById(userParam.getUserId());
        BigDecimal balance = user.getBalance();
        BigDecimal money = userParam.getMoney();
        if (money == null || money.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("请输入正确的金额");
        }
        // 判断充值方式，计算最终金额
        if ("inc".equals(userParam.getBalanceMode())) {
            balance = user.getBalance().add(money);
        } else if ("dec".equals(userParam.getBalanceMode())) {
            balance = balance.subtract(money).compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : balance.subtract(money);
            money = money.negate();
        } else {
            balance = money;
        }
        // 更新账户余额
        user.setBalance(balance);
        this.updateById(user);
        // 更新记录
        UserBalanceLog userBalanceLog = new UserBalanceLog();
        userBalanceLog.setScene(30);
        userBalanceLog.setUserId(userParam.getUserId());
        userBalanceLog.setMoney(money);
        userBalanceLog.setRemark(userParam.getBalanceRemark());
        return userBalanceLogService.save(userBalanceLog);
    }

    /**
     * 获取用户统计数据
     * @param startDate
     * @param endDate
     * @param type
     * @return
     */
    //获取订单统计数据
    public Integer getUserData(String startDate, String endDate, String type) throws ParseException {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        //开始查询时间不为空
        if (StringUtils.isNotEmpty(startDate)) {
            wrapper.ge(User::getCreateTime, DateUtil.parse(startDate + " 00:00:00"));
        }
        //结束查询时间不为空
        if (StringUtils.isNotEmpty(endDate)) {
            wrapper.le(User::getCreateTime, DateUtil.parse(endDate+" 23:59:59"));
        } else if (StringUtils.isNotEmpty(startDate)) {
            //如果结束查询时间为空,开始查询时间不为空，就默认设置时间查询区间为开始时间+1天
            wrapper.le(User::getCreateTime,DateUtil.parse(startDate+" 23:59:59"));
        }
        wrapper.eq(User::getIsDelete, 0);
        //根据查询模式返回不同的数值
        if ("user_total".equals(type)||"user_add".equals(type)) {
            return this.count(wrapper);
        } else if ("user_pay".equals(type)) {
            wrapper.gt(User::getPayMoney,0);
            return this.count(wrapper);
        } else if ("user_no_pay".equals(type)) {
            wrapper.eq(User::getPayMoney,0);
           return this.count(wrapper);
        }
        return 0;
    }

    /**
     * 消减用户的消费金额
     * @param user
     * @param expendMoney
     * @return
     */
    public Boolean setDecUserExpend(User user, BigDecimal expendMoney){
        return this.update(new LambdaUpdateWrapper<User>()
                .eq(User::getUserId, user.getUserId())
                .set(User::getExpendMoney, user.getExpendMoney().subtract(expendMoney)));
    }
}
