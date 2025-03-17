

package net.jjjshop.shop.controller.setting;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.cache.RegionCache;
import net.jjjshop.common.entity.settings.DeliveryRule;
import net.jjjshop.common.vo.RegionVo;
import net.jjjshop.framework.common.api.ApiResult;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.framework.log.annotation.OperationLog;
import net.jjjshop.shop.param.setting.DeliveryPageParam;
import net.jjjshop.shop.param.setting.DeliveryParam;
import net.jjjshop.shop.service.settings.DeliveryService;
import net.jjjshop.shop.vo.setting.DeliveryRuleVo;
import net.jjjshop.shop.vo.setting.DeliveryVo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@Api(value = "delivery", tags = {"delivery"})
@RestController
@RequestMapping("/shop/setting/delivery")
public class DeliveryController {

    @Autowired
    private DeliveryService deliveryService;
    @Autowired
    private RegionCache regionCache;

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @RequiresPermissions("/setting/delivery/index")
    @OperationLog(name = "index")
    @ApiOperation(value = "index", response = String.class)
    public ApiResult<Paging<DeliveryVo>> index(@Validated @RequestBody DeliveryPageParam deliveryPageParam) throws Exception{
        return ApiResult.ok(deliveryService.getList(deliveryPageParam));
    }

    @RequestMapping(value = "/toAdd", method = RequestMethod.GET)
    @RequiresPermissions("/setting/delivery/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<Map<String, Object>> toAdd() {
        Map<String, Object> result = new HashMap<>();
        // 获取所有地区
        result.put("regionData", regionCache.getCacheTree());
        // 地区总数
        result.put("cityCount", regionCache.getCacheCount("city"));
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @RequiresPermissions("/setting/delivery/add")
    @OperationLog(name = "add")
    @ApiOperation(value = "add", response = String.class)
    public ApiResult<String> add(@RequestBody @Validated DeliveryParam deliveryParam) {
        if(deliveryService.add(deliveryParam)) {
            return ApiResult.ok(null, "新增成功");
        }else{
            return ApiResult.fail("新增失败");
        }
    }

    @RequestMapping(value = "/toEdit", method = RequestMethod.GET)
    @RequiresPermissions("/setting/delivery/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<Map<String, Object>> toEdit(Integer deliveryId) {
        Map<String, Object> result = new HashMap<>();
        // 地区总数
        result.put("cityCount", regionCache.getCacheCount("city"));
        // 详情
        result.put("detail", deliveryService.getDetail(deliveryId));
        // 格式化
        result.put("formData", this.formatData((DeliveryVo)result.get("detail")));
        // 获取所有地区
        List<RegionVo> regionData = regionCache.getCacheTree();
        // 初始选中
        this.checkedRegion(regionData, (List<DeliveryRuleVo>)result.get("formData"));
        result.put("regionData", regionData);
        return ApiResult.ok(result);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @RequiresPermissions("/setting/delivery/edit")
    @OperationLog(name = "edit")
    @ApiOperation(value = "edit", response = String.class)
    public ApiResult<String> edit(@RequestBody @Validated DeliveryParam deliveryParam) {
        if(deliveryService.edit(deliveryParam)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @RequiresPermissions("/setting/delivery/delete")
    @OperationLog(name = "delete")
    @ApiOperation(value = "delete", response = String.class)
    public ApiResult<String> delete(Integer deliveryId) {
        if(deliveryService.delById(deliveryId)) {
            return ApiResult.ok(null, "修改成功");
        }else{
            return ApiResult.fail("修改失败");
        }
    }

    /**
     * 格式化数据
     */
    private List<DeliveryRuleVo> formatData(DeliveryVo vo){
        // 所有区域
        List<RegionVo> regionList = regionCache.getCacheAll();
        List<DeliveryRuleVo> ruleList = new ArrayList<>();
        for (DeliveryRule rule:vo.getRule()) {
            List<String> citys = Arrays.asList(rule.getRegion().split(","));
            List<Integer> province = new ArrayList<>();
            for(String cityId:citys){
                RegionVo cityVo = this.getCityById(regionList, Integer.parseInt(cityId));
                if(cityVo == null){
                    continue;
                }
                if(!province.contains(cityVo.getPid())){
                    province.add(cityVo.getPid());
                }
            }
            DeliveryRuleVo ruleVo = new DeliveryRuleVo();
            BeanUtils.copyProperties(rule, ruleVo);
            ruleVo.setCitys(citys);
            ruleVo.setProvince(province);
            ruleList.add(ruleVo);
        }

        return ruleList;
    }

    /**
     * 朝招地区vo
     * @param regionList
     * @return
     */
    private RegionVo getCityById(List<RegionVo> regionList, Integer cityId){
        RegionVo bean = null;
        for(RegionVo vo:regionList) {
            if(vo.getId().intValue() == cityId.intValue()){
                bean = vo;
                break;
            }
        }
        return bean;
    }

    /**
     * 修改时是否选中
     */
    private void checkedRegion(List<RegionVo> regionData, List<DeliveryRuleVo> formData){
        for(RegionVo regionVo:regionData){
            regionVo.setIndex(new ArrayList<Integer>());
            int index = 0;
            for(DeliveryRuleVo ruleVo:formData){
                // 是否选中
                if(ruleVo.getProvince().contains(regionVo.getId())){
                    regionVo.setChecked(true);
                }else{
                    regionVo.setChecked(false);
                }
                regionVo.getIndex().add(index);
                for(RegionVo child:regionVo.getChildren()){
                    // 是否选中
                    if(ruleVo.getCitys().contains(child.getId().toString())){
                        child.setChecked(true);
                        child.setChildIndex(index);
                    }else{
                        child.setChecked(false);
                        child.setChildIndex(-1);
                    }
                }
                index++;
            }
        }
    }
}
