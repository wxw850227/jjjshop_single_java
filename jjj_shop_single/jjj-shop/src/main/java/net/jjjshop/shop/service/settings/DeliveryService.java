package net.jjjshop.shop.service.settings;

import net.jjjshop.common.entity.settings.Delivery;
import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.setting.DeliveryPageParam;
import net.jjjshop.shop.param.setting.DeliveryParam;
import net.jjjshop.shop.vo.setting.DeliveryVo;

/**
 * 配送模板主表 服务类
 * @author jjjshop
 * @since 2022-06-29
 */
public interface DeliveryService extends BaseService<Delivery> {
    /**
     * 新增
     * @param deliveryParam
     * @return
     */
    Boolean add(DeliveryParam deliveryParam);

    /**
     * 分页查询
     * @param deliveryPageParam
     * @return
     */
    Paging<DeliveryVo> getList(DeliveryPageParam deliveryPageParam);

    /**
     * 获取详情
     * @param deliveryId
     * @return
     */
    DeliveryVo getDetail(Integer deliveryId);

    /**
     * 修改
     * @param deliveryParam
     * @return
     */
    Boolean edit(DeliveryParam deliveryParam);

    /**
     * 删除
     * @param deliveryId
     * @return
     */
    Boolean delById(Integer deliveryId);
}
