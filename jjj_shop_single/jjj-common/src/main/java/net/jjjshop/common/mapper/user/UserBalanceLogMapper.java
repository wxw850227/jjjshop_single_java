package net.jjjshop.common.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.common.entity.user.UserBalanceLog;
import net.jjjshop.common.vo.user.UserBalanceLogVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 用户余额变动明细表 Mapper 接口
 *
 * @author jjjshop
 * @since 2022-07-21
 */
@Repository
public interface UserBalanceLogMapper extends BaseMapper<UserBalanceLog> {

    /**
     * 通过昵称,变动场景,日期区间查询用户余额明细
     * @param nickName,scene,startDate,endDate
     * @return
     */
    List<UserBalanceLogVo> getList(@Param("nickName") String nickName,
                                   @Param("pageIndex") Long pageIndex,
                                   @Param("pageSize") Long pageSize,
                                   @Param("scene") Integer scene,
                                   @Param("startDate") String startDate,
                                   @Param("endDate") String endDate);
}
