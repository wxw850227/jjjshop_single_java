package net.jjjshop.front.service.settings.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.settings.Express;
import net.jjjshop.common.mapper.settings.ExpressMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.front.service.settings.ExpressService;
import net.jjjshop.front.vo.settings.ExpressListVo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 物流公司记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-19
 */
@Slf4j
@Service
public class ExpressServiceImpl extends BaseServiceImpl<ExpressMapper, Express> implements ExpressService {

    /**
     * 获取所有物流公司
     * @param
     * @return
     */
    public List<ExpressListVo> getAll(){
        return this.list(new LambdaQueryWrapper<Express>().orderByAsc(Express::getSort)).stream().map(e -> {
            ExpressListVo vo = new ExpressListVo();
            BeanUtils.copyProperties(e,vo);
            return vo;
        }).collect(Collectors.toList());
    }

}
