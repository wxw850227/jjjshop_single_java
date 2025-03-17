package net.jjjshop.common.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.common.entity.user.UserGradeLog;
import org.springframework.stereotype.Repository;


/**
 * 用户会员等级变更记录表 Mapper 接口
 *
 * @author jjjshop
 * @since 2022-07-21
 */
@Repository
public interface UserGradeLogMapper extends BaseMapper<UserGradeLog> {

}
