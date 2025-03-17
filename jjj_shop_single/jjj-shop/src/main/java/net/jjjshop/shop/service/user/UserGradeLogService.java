package net.jjjshop.shop.service.user;

import net.jjjshop.common.entity.user.UserGradeLog;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.user.UserGradeLogPageParam;
import net.jjjshop.shop.vo.user.UserGradeLogVo;

/**
 * 用户会员等级变更记录表 服务类
 * @author jjjshop
 * @since 2022-07-21
 */
public interface UserGradeLogService extends BaseService<UserGradeLog> {

    /**
     * 分页查询用户等级日志
     * @param userGradeLogPageParam
     * @return
     */
    Paging<UserGradeLogVo> getList(UserGradeLogPageParam userGradeLogPageParam);
}
