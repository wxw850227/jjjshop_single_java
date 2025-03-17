package net.jjjshop.common.service.user;

import net.jjjshop.common.entity.user.UserGrade;
import net.jjjshop.framework.common.service.BaseService;

/**
 * 用户会员等级表 服务类
 *
 * @author jjjshop
 * @since 2022-06-29
 */
public interface UserGradeService extends BaseService<UserGrade> {

    Integer getDefaultGradeId();
}
