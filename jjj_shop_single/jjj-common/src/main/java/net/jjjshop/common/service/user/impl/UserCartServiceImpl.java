package net.jjjshop.common.service.user.impl;

import net.jjjshop.common.entity.user.UserCart;
import net.jjjshop.common.mapper.user.UserCartMapper;
import net.jjjshop.common.service.user.UserCartService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 用户购物车 服务实现类
 *
 * @author jjjshop
 * @since 2022-08-02
 */
@Slf4j
@Service
public class UserCartServiceImpl extends BaseServiceImpl<UserCartMapper, UserCart> implements UserCartService {

    @Autowired
    private UserCartMapper userCartMapper;

}
