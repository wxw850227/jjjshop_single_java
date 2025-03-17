package net.jjjshop.shop.service.store.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.cache.RegionCache;
import net.jjjshop.common.entity.store.Store;
import net.jjjshop.common.mapper.store.StoreMapper;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.common.vo.RegionVo;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.CommonPageParam;
import net.jjjshop.shop.param.store.StoreParam;
import net.jjjshop.shop.service.store.StoreService;
import net.jjjshop.shop.vo.store.StoreVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private RegionCache regionCache;

    @Autowired
    private UploadFileUtils uploadFileUtils;

    /**
     * 商家门店分页查询
     *
     * @param commonPageParam
     * @return
     */
    public Paging<StoreVo> getList(CommonPageParam commonPageParam) {
        Page page = new PageInfo(commonPageParam);
        IPage<Store> iPage = this.page(page, new LambdaQueryWrapper<Store>()
                .eq(Store::getIsDelete, 0)
                .orderByAsc(Store::getSort)
                .orderByDesc(Store::getCreateTime));
        List<RegionVo> provinceList = regionCache.getCacheTree();
        IPage<StoreVo> result = iPage.convert(item -> {
            StoreVo vo = new StoreVo();
            BeanUtils.copyProperties(item, vo);
            vo.setLogoImagePath(uploadFileUtils.getFilePath(item.getLogoImageId()));
            vo.setAddress(getDetailAddress(vo, provinceList));
            vo.setIsCheckText(getIsCheckText(vo.getIsCheck()));
            vo.setStatusText(getstatusText(vo.getStatus()));
            return vo;
        });
        return new Paging(result);
    }


    private String getIsCheckText(Integer status) {
        if (status == 1) {
            return "支持";
        } else {
            return "不支持";
        }
    }

    private String getstatusText(Integer status) {
        if (status == 1) {
            return "启用";
        } else {
            return "禁用";
        }
    }

    /**
     * 获取门店详细地址
     *
     * @param vo,provinceList
     * @return
     */
    private String getDetailAddress(StoreVo vo, List<RegionVo> list) {
        String province = "";
        String city = "";
        String region = "";

        for (RegionVo p : list) {
            //循环获取省份
            if (p.getId() == vo.getProvinceId()) {
                province = p.getName();
                list = p.getChildren();
                //获取省份后循环获取市名
                for (RegionVo c : list) {
                    if (c.getId() == vo.getCityId()) {
                        city = c.getName();
                        list = c.getChildren();
                        //获取市名后循环获取区名
                        for (RegionVo r : list) {
                            if (r.getId() == vo.getRegionId()) {
                                region = r.getName();
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }
        return province + city + region + vo.getAddress();
    }

    /**
     * 获取所有商家门店
     *
     * @param
     * @return
     */
    public List<StoreVo> getAll() {
        List<Store> list = this.list(new LambdaQueryWrapper<Store>()
                .eq(Store::getIsDelete, 0)
                .orderByAsc(Store::getSort)
                .orderByDesc(Store::getCreateTime));
        return list.stream().map(e -> {
            StoreVo vo = new StoreVo();
            BeanUtils.copyProperties(e, vo);
            if(e.getLogoImageId() != null && e.getLogoImageId() != 0){
                vo.setLogoImagePath(uploadFileUtils.getFilePath(e.getLogoImageId()));
            }
            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 获取添加商家门店需要的地址信息
     *
     * @param
     * @return
     */
    public List<RegionVo> toAdd() {
        return regionCache.getCacheTree();
    }

    /**
     * 添加商家门店
     *
     * @param storeParam
     * @return
     */
    public Boolean add(StoreParam storeParam) {
        Store store = new Store();
        String[] split = storeParam.getCoordinate().split(",");
        if (split.length < 2) {
            storeParam.setLatitude(split[0]);
        } else {
            storeParam.setLatitude(split[0]);
            storeParam.setLongitude(split[1]);
        }
        BeanUtils.copyProperties(storeParam, store);
        return this.save(store);
    }

    /**
     * 获取编辑商家门店需要的信息
     *
     * @param storeId
     * @return
     */
    public Map<String, Object> toEdit(Integer storeId) {
        Map<String, Object> map = new HashMap();
        StoreVo vo = new StoreVo();
        Store store = this.getById(storeId);
        BeanUtils.copyProperties(store, vo);
        if (StringUtils.isNotEmpty(store.getLatitude())) {
            vo.setCoordinate(store.getLatitude() + "," + store.getLongitude());
        }
        vo.setLogoImagePath(uploadFileUtils.getFilePath(vo.getLogoImageId()));
        map.put("store", vo);
        map.put("regionList", regionCache.getCacheTree());
        return map;
    }

    /**
     * 编辑商家门店
     *
     * @param storeParam
     * @return
     */
    public Boolean edit(StoreParam storeParam) {
        Store store = new Store();
        if (StringUtils.isNotEmpty(storeParam.getCoordinate())) {
            String[] split = storeParam.getCoordinate().split(",");
            if (split.length < 2) {
                storeParam.setLatitude(split[0]);
            } else {
                storeParam.setLatitude(split[0]);
                storeParam.setLongitude(split[1]);
            }
        }
        BeanUtils.copyProperties(storeParam, store);
        return this.updateById(store);
    }

    /**
     * 软删除商家门店
     *
     * @param storeId
     * @return
     */
    public Boolean setDelete(Integer storeId) {
        return this.update(new LambdaUpdateWrapper<Store>().eq(Store::getStoreId, storeId).set(Store::getIsDelete, 1));
    }
}
