package net.jjjshop.common.service.store.impl;

import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.store.Store;
import net.jjjshop.common.mapper.store.StoreMapper;
import net.jjjshop.common.service.store.StoreService;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 商家门店记录表 服务实现类
 *
 * @author jjjshop
 * @since 2022-07-27
 */
@Slf4j
@Service
public class StoreServiceImpl extends BaseServiceImpl<StoreMapper, Store> implements StoreService {

}
