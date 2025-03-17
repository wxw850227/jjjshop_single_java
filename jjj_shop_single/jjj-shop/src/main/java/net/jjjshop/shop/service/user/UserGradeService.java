package net.jjjshop.shop.service.user;

import net.jjjshop.common.entity.user.UserGrade;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.user.UserGradePageParam;
import net.jjjshop.shop.param.user.UserGradeParam;

import java.util.List;

/**
 * 用户会员等级表 服务类
 * @author jjjshop
 * @since 2022-06-29
 */
public interface UserGradeService extends BaseService<UserGrade> {

    /**
     * 分页查询用户等级
     * @param userGradePageParam
     * @return
     */
    Paging<UserGrade> getList(UserGradePageParam userGradePageParam);

    /**
     * 获取所有用户等级
     * @param
     * @return
     */
    List<UserGrade> getAll();

    /**
     * 添加用户等级
     * @param userGradeParam
     * @return
     */
    Boolean add(UserGradeParam userGradeParam);

    /**
     * 修改用户等级
     * @param userGradeParam
     * @return
     */
    Boolean edit(UserGradeParam userGradeParam);

    /**
     * 软删除用户等级
     * @param id
     * @return
     */
    Boolean setDelete(Integer id);
}
