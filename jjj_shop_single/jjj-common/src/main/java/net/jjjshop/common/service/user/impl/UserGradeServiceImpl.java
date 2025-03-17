package net.jjjshop.common.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.UserGrade;
import net.jjjshop.common.mapper.user.UserGradeMapper;
import net.jjjshop.common.service.user.UserGradeService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 用户会员等级表 服务实现类
 *
 * @author jjjshop
 * @since 2022-06-29
 */
@Slf4j
@Service
public class UserGradeServiceImpl extends BaseServiceImpl<UserGradeMapper, UserGrade> implements UserGradeService {

    /**
     * 获取默认等级id
     */
    public Integer getDefaultGradeId(){
        return this.getOne(new LambdaQueryWrapper<UserGrade>().eq(UserGrade::getIsDefault, 1)).getGradeId();
    }

}
