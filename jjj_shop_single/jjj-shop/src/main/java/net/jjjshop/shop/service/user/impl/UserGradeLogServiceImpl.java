package net.jjjshop.shop.service.user.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.UserGrade;
import net.jjjshop.common.entity.user.UserGradeLog;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.mapper.user.UserGradeLogMapper;
import net.jjjshop.shop.param.user.UserGradeLogPageParam;
import net.jjjshop.shop.service.user.UserGradeLogService;
import net.jjjshop.shop.service.user.UserGradeService;
import net.jjjshop.shop.vo.user.UserGradeLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * 用户会员等级变更记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-21
 */
@Slf4j
@Service
public class UserGradeLogServiceImpl extends BaseServiceImpl<UserGradeLogMapper, UserGradeLog> implements UserGradeLogService {

    @Autowired
    private UserGradeService userGradeService;

    @Autowired
    private UserGradeLogMapper userGradeLogMapper;

    /**
     * 分页查询用户等级日志
     * @param userGradeLogPageParam
     * @return
     */
    public Paging<UserGradeLogVo> getList(UserGradeLogPageParam userGradeLogPageParam) {
        // 开始页码
        userGradeLogPageParam.setStartIndex((userGradeLogPageParam.getPageIndex() - 1) * userGradeLogPageParam.getPageSize());
        // 查询当前页列表
        List<UserGradeLogVo> list = userGradeLogMapper.getList(userGradeLogPageParam);
        List<UserGrade> grades = userGradeService.list();
        HashMap<Integer, String> gradeNames = new HashMap<>();
        grades.stream().forEach(e->{
            gradeNames.put(e.getGradeId(),e.getName());
        });
        list.stream().forEach(e->{
            e.setOldGradeName(gradeNames.get(e.getOldGradeId()));
            e.setNewGradeName(gradeNames.get(e.getNewGradeId()));
        });
        // 将结果封装Paging对象输出
        PageInfo<UserGradeLogVo> pageInfo = new PageInfo<>();
        pageInfo.setRecords(list);
        pageInfo.setTotal(userGradeLogMapper.getTotalCount(userGradeLogPageParam));
        return new Paging<>(pageInfo);
    }

}
