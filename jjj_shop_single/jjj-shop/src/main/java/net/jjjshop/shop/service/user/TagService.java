package net.jjjshop.shop.service.user;

import net.jjjshop.common.entity.user.Tag;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.user.TagPageParam;
import net.jjjshop.shop.param.user.TagParam;
import net.jjjshop.shop.vo.user.TagVo;

import java.util.List;

/**
 * 用户tag表 服务类
 * @author jjjshop
 * @since 2022-07-21
 */
public interface TagService extends BaseService<Tag> {

    /**
     * 分页查询用户标签
     * @param tagPageParam
     * @return
     */
    Paging<TagVo> getList(TagPageParam tagPageParam);

    /**
     * 获取所有用户标签
     * @param
     * @return
     */
    List<Tag> getAll();

    /**
     * 添加用户标签
     * @param tagParam
     * @return
     */
    Boolean add(TagParam tagParam);

    /**
     * 编辑用户标签
     * @param tagParam
     * @return
     */
    Boolean edit(TagParam tagParam);

    /**
     * 真删除用户标签
     * @param id
     * @return
     */
    Boolean delById(Integer id);
}
