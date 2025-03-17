package net.jjjshop.common.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.common.entity.user.UserFavorite;

import org.springframework.stereotype.Repository;


/**
 * 我的收藏 Mapper 接口
 *
 * @author jjjshop
 * @since 2022-08-02
 */
@Repository
public interface UserFavoriteMapper extends BaseMapper<UserFavorite> {


}
