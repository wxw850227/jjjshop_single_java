package net.jjjshop.shop.service.settings;

import net.jjjshop.common.entity.settings.ReturnAddress;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.setting.ReturnAddressPageParam;
import net.jjjshop.shop.param.setting.ReturnAddressParam;

import java.util.List;

/**
 * 退货地址记录表 服务类
 * @author jjjshop
 * @since 2022-07-20
 */
public interface ReturnAddressService extends BaseService<ReturnAddress> {

    /**
     * 分页查询退货地址
     * @param returnAddressPageParam
     * @return
     */
    Paging<ReturnAddress> getList(ReturnAddressPageParam returnAddressPageParam);

    /**
     * 查询所有退货地址
     * @param
     * @return
     */
    List<ReturnAddress> getAll();

    /**
     * 添加退货地址
     * @param returnAddressParam
     * @return
     */
    boolean add(ReturnAddressParam returnAddressParam);

    /**
     * 修改退货地址
     * @param returnAddressParam
     * @return
     */
    boolean edit(ReturnAddressParam returnAddressParam);

    /**
     * 删除退货地址
     * @param id
     * @return
     */
    boolean setDelete(Integer id);
}
