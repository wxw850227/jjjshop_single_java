package net.jjjshop.common.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.entity.user.UserGrade;
import net.jjjshop.common.entity.user.UserGradeLog;
import net.jjjshop.common.service.user.UserGradeLogService;
import net.jjjshop.common.service.user.UserGradeService;
import net.jjjshop.common.service.user.UserService;
import net.jjjshop.framework.core.util.RequestDetailThreadLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
public class UserUtils {

    @Autowired
    private UserService userService;
    @Autowired
    private UserGradeService userGradeService;
    @Autowired
    private UserGradeLogService userGradeLogService;

    /**
     * 可用等级
     * @return
     */
    public List<UserGrade> getUsableGradeList(Long appId){
        if(appId == null){
            appId = RequestDetailThreadLocal.getRequestDetail().getAppId();
        }
        return userGradeService.list(new LambdaQueryWrapper<UserGrade>()
                .eq(UserGrade::getIsDelete, false).eq(UserGrade::getAppId, appId)
                .orderByAsc(UserGrade::getWeight).orderByAsc(UserGrade::getCreateTime));
    }

    /**
     * 用户升级处理
     */
    @Async
    public void userGradeUpgrade(Integer userId){
        User user = userService.getById(userId);
        // 获取所有等级
        List<UserGrade> list = this.getUsableGradeList(Long.valueOf(user.getAppId()));
        if (list.size() == 0) {
            return;
        }
        // 遍历等级，根据升级条件 查询满足消费金额的用户列表，并且他的等级小于该等级
        UserGrade upgradeGrade = null;
        for(UserGrade grade:list){
            if(grade.getIsDefault() == 1){
                continue;
            }
            boolean isUpgrade = this.checkCanUpdate(user, grade);
            if(isUpgrade){
                upgradeGrade = grade;
            }else{
                break;
            }
        }
        if(upgradeGrade != null && upgradeGrade.getGradeId().intValue() != user.getGradeId().intValue()){
            log.info(String.format("setUserGrade userId:%s gradeId:%s", user.getUserId(), upgradeGrade.getGradeId()));
            // 修改会员的等级
            this.upgradeGrade(user, upgradeGrade);
        }
    }

    /**
     * 查询满足会员等级升级条件的用户列表
     */
    public boolean checkCanUpdate(User user, UserGrade grade)
    {
        // 按消费升级
        if(grade.getOpenMoney() == 1 && user.getExpendMoney().compareTo(new BigDecimal(grade.getUpgradeMoney())) >= 0){
            return true;
        }
        return false;
    }

    /**
     * 批量设置会员等级
     */
    private void upgradeGrade(User user, UserGrade upgradeGrade){
        // 更新会员等级的数据
        userService.update(new LambdaUpdateWrapper<User>().eq(User::getUserId, user.getUserId())
                .set(User::getGradeId, upgradeGrade.getGradeId()));
        UserGradeLog log = new UserGradeLog();
        log.setOldGradeId(user.getGradeId());
        log.setNewGradeId(upgradeGrade.getGradeId());
        //自动升级
        log.setChangeType(20);
        log.setUserId(user.getUserId());
        log.setAppId(user.getAppId());
        userGradeLogService.save(log);
    }
}
