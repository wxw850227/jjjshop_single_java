package net.jjjshop.common.settings.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.config.properties.SpringBootJjjProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("底部导航设置VO")
public class NavVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer isAuto;
    private Integer type;
    private String backgroundColor;
    private String textColor;
    private String textHoverColor;
    private Boolean bulge;
    private List<TabBar> list;

    @Data
    @Accessors(chain = true)
    @ApiModel("tabBar")
    public static class TabBar implements Serializable {
        private static final long serialVersionUID = 1L;
        private String text;
        private String iconPath;
        private String selectedIconPath;
        private String linkUrl;
    }

    public NavVo() {
        this.isAuto = 0;
        this.type = 0;
        this.backgroundColor = "";
        this.textColor = "";
        this.bulge = true;
        this.list = new ArrayList<>();
        String imagePath = SpringBootJjjProperties.getStaticAccessUrl();
        // 首页
        TabBar home = new TabBar();
        home.setText("首页");
        home.setIconPath(imagePath + "image/tabbar/home.png");
        home.setSelectedIconPath(imagePath + "image/tabbar/home_active.png");
        home.setLinkUrl("/pages/index/index");
        list.add(home);
        // 分类
        TabBar category = new TabBar();
        category.setText("分类");
        category.setIconPath(imagePath + "image/tabbar/category.png");
        category.setSelectedIconPath(imagePath + "image/tabbar/category_active.png");
        category.setLinkUrl("/pages/product/category");
        list.add(category);
        // 购物车
        TabBar cart = new TabBar();
        cart.setText("购物车");
        cart.setIconPath(imagePath + "image/tabbar/cart.png");
        cart.setSelectedIconPath(imagePath + "image/tabbar/cart_active.png");
        cart.setLinkUrl("/pages/cart/cart");
        list.add(cart);
        // 我的
        TabBar user = new TabBar();
        user.setText("我的");
        user.setIconPath(imagePath + "image/tabbar/user.png");
        user.setSelectedIconPath(imagePath + "image/tabbar/user_active.png");
        user.setLinkUrl("/pages/user/index/index");
        list.add(user);
    }

}
