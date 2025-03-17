package net.jjjshop.shop.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.common.entity.user.UserGradeLog;
import net.jjjshop.shop.param.user.UserGradeLogPageParam;
import net.jjjshop.shop.vo.user.UserGradeLogVo;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 用户会员等级变更记录表 Mapper 接口
 *
 * @author jjjshop
 * @since 2022-07-21
 */
@Repository
public interface UserGradeLogMapper extends BaseMapper<UserGradeLog> {
    /**
     * 通过昵称查询用户等级变动明细
     * @param userGradeLogPageParam
     * @return
     */
    List<UserGradeLogVo> getList(UserGradeLogPageParam userGradeLogPageParam);

    /**
     * 通过昵称查询用户等级变动明细数量
     * @param userGradeLogPageParam
     * @return
     */
    Long getTotalCount(UserGradeLogPageParam userGradeLogPageParam);
}
