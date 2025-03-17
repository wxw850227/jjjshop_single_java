package net.jjjshop.common.mapper.store;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.common.entity.store.StoreOrder;

import org.springframework.stereotype.Repository;


/**
 * 商家门店核销订单记录表 Mapper 接口
 *
 * @author jjjshop
 * @since 2022-07-27
 */
@Repository
public interface StoreOrderMapper extends BaseMapper<StoreOrder> {


}
