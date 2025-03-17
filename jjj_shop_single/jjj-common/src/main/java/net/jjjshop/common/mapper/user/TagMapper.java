package net.jjjshop.common.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.common.entity.user.Tag;
import org.springframework.stereotype.Repository;

/**
 * 标签表 Mapper 接口
 *
 * @author jjjshop
 * @since 2022-07-21
 */
@Repository
public interface TagMapper extends BaseMapper<Tag> {
}
