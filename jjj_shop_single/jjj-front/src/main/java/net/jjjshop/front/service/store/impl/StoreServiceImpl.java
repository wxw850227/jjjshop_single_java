package net.jjjshop.front.service.store.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.store.Store;
import net.jjjshop.common.mapper.store.StoreMapper;
import net.jjjshop.common.service.settings.RegionService;
import net.jjjshop.common.util.CoordinateUtils;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.front.service.store.StoreService;
import net.jjjshop.front.vo.store.StoreVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 商家门店记录表 服务实现类
 * @author jjjshop
 * @since 2022-07-27
 */
@Slf4j
@Service
public class StoreServiceImpl extends BaseServiceImpl<StoreMapper, Store> implements StoreService {
    @Autowired
    private UploadFileUtils uploadFileUtils;
    @Autowired
    private RegionService regionService;

    /**
     * 通过id集合查找门店
     * @param storeIds
     * @return
     */
    public List<StoreVo> getListByStoreIds(List<Integer> storeIds){
        List<Store> list = this.list(new LambdaQueryWrapper<Store>().in(Store::getStoreId, storeIds)
                .eq(Store::getIsDelete, 0).eq(Store::getStatus, 1));
        return this.transToVoList(list);
    }

    /**
     * 通过id查找门店
     * @param storeId
     * @return
     */
    public StoreVo getStoreVoById(Integer storeId){
        return this.transToVo(this.getById(storeId));
    }

    /**
     * 查找门店
     * @param limit
     * @return
     */
    public List<StoreVo> getList(Integer limit){
        List<Store> list = this.list(new LambdaQueryWrapper<Store>().eq(Store::getIsDelete, 0)
                .eq(Store::getStatus, 1).last("limit "+limit)
                .orderByAsc(Store::getSort).orderByDesc(Store::getCreateTime));
        return this.transToVoList(list);
    }

    private List<StoreVo> transToVoList(List<Store> list){
        // 转成vo
        return list.stream().map(e -> {
            return this.transToVo(e);
        }).collect(Collectors.toList());
    }

    private StoreVo transToVo(Store store){
        StoreVo vo = new StoreVo();
        BeanUtils.copyProperties(store, vo);
        vo.setLogoImage(uploadFileUtils.getFilePath(store.getLogoImageId()));
        vo.setProvince(regionService.getById(store.getProvinceId()).getName());
        vo.setCity(regionService.getById(store.getCityId())==null?"":regionService.getById(store.getCityId()).getName());
        vo.setRegion(regionService.getById(store.getRegionId())==null?"":regionService.getById(store.getRegionId()).getName());
        return vo;
    }

    /**
     * 查询所有可核销门店
     * @param longitude
     * @param latitude
     * @return
     */
    public List<StoreVo> getAllCheck(String longitude, String latitude){
        List<Store> list = this.list(new LambdaQueryWrapper<Store>().eq(Store::getIsCheck, 1)
                .eq(Store::getStatus, 1).orderByAsc(Store::getSort).orderByDesc(Store::getCreateTime));
        List<StoreVo> voList = this.transToVoList(list);
        if (StringUtils.isNotEmpty(longitude) && StringUtils.isNotEmpty(latitude)) {
            for (StoreVo vo : voList) {
                double distinct = CoordinateUtils.distance(Double.parseDouble(latitude),
                        Double.parseDouble(longitude), Double.parseDouble(vo.getLatitude()), Double.parseDouble(vo.getLongitude()));
                vo.setDistinct(distinct);
                if (distinct >= 1000) {
                    distinct = distinct / 1000;
                    DecimalFormat df = new DecimalFormat("0.0");
                    vo.setShowDistinct(df.format(distinct) + "km");
                } else {
                    vo.setShowDistinct(distinct + "m");
                }
            }
            voList.sort((o1, o2) -> {
                if (o1.getDistinct() > o2.getDistinct()) {
                    return -1;
                } else if (o1.getDistinct() < o2.getDistinct()) {
                    return 1;
                }
                return 0;
            });
        }
        return voList;
    }

}
