package net.jjjshop.common.service.user.impl;

import net.jjjshop.common.entity.user.UserFavorite;
import net.jjjshop.common.mapper.user.UserFavoriteMapper;
import net.jjjshop.common.service.user.UserFavoriteService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 我的收藏 服务实现类
 * @author jjjshop
 * @since 2022-08-02
 */
@Slf4j
@Service
public class UserFavoriteServiceImpl extends BaseServiceImpl<UserFavoriteMapper, UserFavorite> implements UserFavoriteService {

    @Autowired
    private UserFavoriteMapper userFavoriteMapper;

}
