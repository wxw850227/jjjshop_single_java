package net.jjjshop.front.service.store.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.store.StoreClerk;
import net.jjjshop.common.mapper.store.StoreClerkMapper;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.front.service.store.StoreClerkService;
import org.springframework.stereotype.Service;

/**
 * 商家门店店员表 服务实现类
 * @author jjjshop
 * @since 2022-07-27
 */
@Slf4j
@Service
public class StoreClerkServiceImpl extends BaseServiceImpl<StoreClerkMapper, StoreClerk> implements StoreClerkService {

    /**
     * 获取门店核销员
     * @param storeId
     * @param userId
     * @return
     */
    public StoreClerk detail(Integer storeId, Integer userId) {
        StoreClerk storeClerk = this.getOne(new LambdaQueryWrapper<StoreClerk>()
                .eq(StoreClerk::getUserId, userId)
                .eq(StoreClerk::getStoreId, storeId));
        if (storeClerk != null && storeClerk.getIsDelete() == 0) {
            if (storeClerk.getStatus() == 0) {
                throw new BusinessException("当前店员状态已被禁用");
            }
            if (storeClerk.getStoreId() != storeId) {
                throw new BusinessException("当前店员不属于该门店，没有核销权限");
            }
            return storeClerk;
        } else {
            throw new BusinessException("未找到店员信息");
        }
    }
}
