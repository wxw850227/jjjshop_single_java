package net.jjjshop.shop.service.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.Tag;
import net.jjjshop.common.entity.user.UserTag;
import net.jjjshop.common.mapper.user.TagMapper;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.user.TagPageParam;
import net.jjjshop.shop.param.user.TagParam;
import net.jjjshop.shop.service.user.TagService;
import net.jjjshop.shop.service.user.UserTagService;
import net.jjjshop.shop.vo.user.TagVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户tag表 服务实现类
 * @author jjjshop
 * @since 2022-07-21
 */
@Slf4j
@Service
public class TagServiceImpl extends BaseServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private UserTagService userTagService;

    /**
     * 分页查询用户标签
     * @param tagPageParam
     * @return
     */
    public Paging<TagVo> getList(TagPageParam tagPageParam) {
        Page<Tag> page = new PageInfo<>(tagPageParam);
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Tag::getCreateTime);
        IPage<Tag> iPage = this.page(page, wrapper);
        IPage<TagVo> result = iPage.convert(item -> {
            TagVo vo = new TagVo();
            BeanUtils.copyProperties(item, vo);
            vo.setCount(userTagService.count(new LambdaQueryWrapper<UserTag>()
                    .eq(UserTag::getTagId, item.getTagId())));
            return vo;
        });
        return new Paging(result);
    }

    /**
     * 获取所有用户标签
     * @param
     * @return
     */
    public List<Tag> getAll() {
        return this.list(new LambdaQueryWrapper<Tag>()
                .select(Tag::getTagId, Tag::getTagName)
                .orderByAsc(Tag::getCreateTime));
    }

    /**
     * 添加用户标签
     * @param tagParam
     * @return
     */
    public Boolean add(TagParam tagParam) {
        Tag tag = new Tag();
        BeanUtils.copyProperties(tagParam, tag);
        return this.save(tag);
    }

    /**
     * 编辑用户标签
     * @param tagParam
     * @return
     */
    public Boolean edit(TagParam tagParam) {
        Tag tag = new Tag();
        BeanUtils.copyProperties(tagParam, tag);
        return this.updateById(tag);
    }

    /**
     * 真删除用户标签
     * @param id
     * @return
     */
    public Boolean delById(Integer id) {
        userTagService.remove(new LambdaQueryWrapper<UserTag>().eq(UserTag::getTagId, id));
        return this.removeById(id);
    }

}
