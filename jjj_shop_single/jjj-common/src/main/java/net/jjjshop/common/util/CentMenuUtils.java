package net.jjjshop.common.util;

import net.jjjshop.common.entity.page.CenterMenu;

import java.util.ArrayList;
import java.util.List;

public class CentMenuUtils {
    /**
     * 默认系统菜单
     * @return
     */
    public static List<CenterMenu> getSysMenu(){
        List<CenterMenu> list = new ArrayList<>();
        list.add(getDefaultMenu("address", "收货地址", "/pages/user/address/address", "链接到 页面 地址", "image/center_menu/address.png"));
        list.add(getDefaultMenu("coupon", "领券中心", "/pages/coupon/coupon", "链接到 页面 优惠券", "image/center_menu/coupon.png"));
        list.add(getDefaultMenu("myCoupon", "我的优惠券", "/pages/user/my-coupon/my-coupon", "链接到 页面 我的优惠券", "image/center_menu/my_coupon.png"));
        list.add(getDefaultMenu("myFav", "我的收藏", "/pages/user/favorite/favorite", "链接到 页面 我的收藏", "image/center_menu/my_fav.png"));
        list.add(getDefaultMenu("settings", "设置", "/pages/user/set/set", "链接到 页面 设置", "image/center_menu/settings.png"));
        return list;
    }


    private static CenterMenu getDefaultMenu(String sysTag, String name, String path, String  pathName, String icon){
        CenterMenu menu = new CenterMenu();
        menu.setSysTag(sysTag);
        menu.setName(name);
        menu.setPath(path);
        menu.setPathName(pathName);
        menu.setIcon(icon);
        return menu;
    }
}
