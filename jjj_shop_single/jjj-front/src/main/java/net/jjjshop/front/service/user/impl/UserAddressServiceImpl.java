package net.jjjshop.front.service.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.entity.user.UserAddress;
import net.jjjshop.common.mapper.user.UserAddressMapper;
import net.jjjshop.common.service.settings.RegionService;
import net.jjjshop.config.constant.CommonConstant;
import net.jjjshop.framework.common.exception.BusinessException;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.util.LoginUtil;
import net.jjjshop.framework.util.ShopLoginUtil;
import net.jjjshop.front.param.user.UserAddressParam;
import net.jjjshop.front.service.user.UserAddressService;
import net.jjjshop.front.service.user.UserService;
import net.jjjshop.front.vo.user.UserAddressVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户收货地址表 服务实现类
 * @author jjjshop
 * @since 2022-08-02
 */

@Slf4j
@Service
public class UserAddressServiceImpl extends BaseServiceImpl<UserAddressMapper, UserAddress> implements UserAddressService {

    @Autowired
    private UserService userService;
    @Autowired
    private RegionService regionService;


    /**
     * 收货地址列表
     * @param userId
     * @return
     */
    public Map<String, Object> getList(Integer userId) {
        HashMap<String, Object> map = new HashMap<>();
        User user = userService.getById(userId);
        Integer addressId = user.getAddressId();
        List<UserAddress> userAddresses = this.list(new LambdaQueryWrapper<UserAddress>().eq(UserAddress::getUserId, userId)
                .comment(CommonConstant.NOT_WITH_App_Id));
        List<UserAddressVo> userAddressVos = userAddresses.stream().map(e -> {
            UserAddressVo vo = new UserAddressVo();
            BeanUtils.copyProperties(e, vo);
            vo.setRegion(this.getDetailRegion(vo));
            return vo;
        }).collect(Collectors.toList());
        map.put("defaultId", addressId);
        map.put("list", userAddressVos);
        return map;
    }

    /**
     * 获取收货地区详情
     * @param vo
     * @return
     */
    private JSONObject getDetailRegion(UserAddressVo vo) {
        JSONObject detailRegion = new JSONObject();
        detailRegion.put("province", regionService.getById(vo.getProvinceId()).getName());
        detailRegion.put("city", regionService.getById(vo.getCityId()).getName());
        detailRegion.put("region", regionService.getById(vo.getRegionId()).getName());
        return detailRegion;
    }

    /**
     * 添加收货地址
     * @param userAddressParam
     * @return
     */
    public Boolean add(User user, UserAddressParam userAddressParam) {
        if (StringUtils.isEmpty(userAddressParam.getPhone())) {
            throw new BusinessException("手机号不正确");
        }
        if (StringUtils.isEmpty(userAddressParam.getName())) {
            throw new BusinessException("收货人不为空");
        }
        if (StringUtils.isEmpty(userAddressParam.getDetail())) {
            throw new BusinessException("收货地址不能为空");
        }
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(userAddressParam, userAddress);
        userAddress.setUserId(user.getUserId());
        userAddress.setAppId(user.getAppId());
        boolean save = this.save(userAddress);
        this.defaultAddress(userAddress.getAddressId(), user);
        return save;
    }

    /**
     * 设置为默认收货地址
     * @param addressId
     * @param user
     * @return
     */
    public Boolean defaultAddress(Integer addressId, User user) {
        return userService.update(new LambdaUpdateWrapper<User>().eq(User::getUserId, user.getUserId()).set(User::getAddressId, addressId));
    }

    /**
     * 获取收货地址详情
     * @param addressId
     * @return
     */
    public UserAddressVo detail(Integer addressId) {
        UserAddress userAddress = this.getOne(new LambdaQueryWrapper<UserAddress>().eq(UserAddress::getAddressId,addressId)
                .comment(CommonConstant.NOT_WITH_App_Id));
        UserAddressVo vo = new UserAddressVo();
        BeanUtils.copyProperties(userAddress, vo);
        vo.setRegion(this.getDetailRegion(vo));
        return vo;
    }

    /**
     * 编辑地址
     * @param userAddressParam
     * @return
     */
    public Boolean edit(UserAddressParam userAddressParam) {
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(userAddressParam, userAddress);
        return this.updateById(userAddress);
    }

    /**
     * 真删除收货地址
     * @param addressId
     * @param user
     * @return
     */
    public Boolean delById(Integer addressId, User user) {
        //如果删除的是默认地址,则重置
        if(Objects.equals(user.getAddressId(), addressId)){
            userService.update(new LambdaUpdateWrapper<User>().eq(User::getUserId, user.getUserId()).set(User::getAddressId, 0));
        }
        return this.removeById(this.getById(addressId));
    }

}
