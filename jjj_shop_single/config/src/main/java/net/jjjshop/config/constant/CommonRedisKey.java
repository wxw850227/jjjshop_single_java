

package net.jjjshop.config.constant;

/**
 *  redis key 常量
 **/
public interface CommonRedisKey {

    // --------------------admin端登录信息开始----------------------
    /**
     * 登录用户token信息key
     * login:token:tokenMd5
     */
    String ADMIN_LOGIN_TOKEN = "single3.admin:login:token:%s";

    /**
     * 登录用户信息key
     * login:user:username
     */
    String ADMIN_LOGIN_USER = "single3.admin:login:user:%s";

    /**
     * 登录用户盐值信息key
     * login:salt:username
     */
    String ADMIN_LOGIN_SALT = "single3.admin:login:salt:%s";

    /**
     * 登录用户username token
     * login:user:token:username:token
     */
    String ADMIN_LOGIN_USER_TOKEN = "single3.admin:login:user:token:%s:%s";


    /**
     * 登录用户下的所有token
     * login:user:token:username:*
     */
    String ADMIN_LOGIN_USER_ALL_TOKEN = "single3.admin:login:user:token:%s:*";

    // --------------------admin端登录信息结束----------------------

    // --------------------shop端登录信息开始----------------------
    /**
     * 登录用户token信息key
     * login:token:tokenMd5
     */
    String SHOP_LOGIN_TOKEN = "single3.shop:login:token:%s";

    /**
     * 登录用户信息key
     * login:user:username
     */
    String SHOP_LOGIN_USER = "single3.shop:login:user:%s";

    /**
     * 登录用户盐值信息key
     * login:salt:username
     */
    String SHOP_LOGIN_SALT = "single3.shop:login:salt:%s";

    /**
     * 登录用户username token
     * login:user:token:username:token
     */
    String SHOP_LOGIN_USER_TOKEN = "single3.shop:login:user:token:%s:%s";


    /**
     * 登录用户下的所有token
     * login:user:token:username:*
     */
    String SHOP_LOGIN_USER_ALL_TOKEN = "single3.shop:login:user:token:%s:*";

    // --------------------shop端登录信息结束----------------------

    // --------------------front端登录信息开始----------------------
    /**
     * 登录用户token信息key
     * login:token:tokenMd5
     */
    String USER_LOGIN_TOKEN = "single3.user:login:token:%s";

    /**
     * 登录用户信息key
     * login:user:username
     */
    String USER_LOGIN_USER = "single3.user:login:user:%s";

    /**
     * 登录用户盐值信息key
     * login:salt:username
     */
    String USER_LOGIN_SALT = "single3.user:login:salt:%s";

    /**
     * 登录用户username token
     * login:user:token:username:token
     */
    String USER_LOGIN_USER_TOKEN = "single3.user:login:user:token:%s:%s";


    /**
     * 登录用户下的所有token
     * login:user:token:username:*
     */
    String USER_LOGIN_USER_ALL_TOKEN = "single3.user:login:user:token:%s:*";

    // --------------------front端登录信息结束----------------------


    /**
     * 验证码
     * verify.code:888888
     */
    String VERIFY_CODE = "single3.verify.code:%s";

    /**
     * 地区缓存
     */
    String REGION_DATA = "single3.region.data";
    /**
     * 设置缓存
     */
    String SETTING_DATA = "single3.setting.data:%s:%s";
    /**
     * 所有设置缓存
     */
    String SETTING_DATA_ALL = "single3.setting.data:%s:*";
    /**
     * 商品分类缓存
     */
    String PRODUCT_CATEGORY_DATA = "single3.product.category.data:%s";
}
