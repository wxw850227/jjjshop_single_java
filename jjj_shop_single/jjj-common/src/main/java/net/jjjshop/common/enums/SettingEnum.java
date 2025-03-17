package net.jjjshop.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设置项枚举
 */
@Getter
@AllArgsConstructor
public enum SettingEnum {
    /**
     * 系统设置
     **/
    SYS_CONFIG("sys_config", "系统设置", "SysConfigVo"),
    STORE("store", "商城设置", "StoreVo"),
    STORAGE("storage", "上传设置", "StorageVo"),
    SMS("sms", "短信设置", "SmsVo"),
    TRADE("trade", "交易设置", "TradeVo"),
    MPSERVICE("mp_service", "客服设置", "MpServiceVo"),
    PRINTING("printer", "打印机设置", "PrinterVo"),
    GETPHONE("getPhone", "获取手机号", "GetPhoneVo"),
    HOMEPUSH("homepush","首页推送","HomePushVo"),
    COLLECTION("collection","引导收藏","CollectionVo"),
    RECOMMEDND("recommend","商品推荐","RecommendVo"),
    APP_SHARE("app_share", "app分享设置", "AppShareVo"),
    THEME("theme", "主题设置", "ThemeVo"),
    PAGE_CATEGORY("page_category", "分类设置", "PageCategoryVo"),
    NAV("nav", "底部导航", "NavVo"),
    OFFICIA("officia","公众号关注","OfficiaVo");



    private String key;
    private String description;
    private String className;

    //根据传入的key动态获取className
    public static String getClassNameByKey(String key) {
        SettingEnum[] enums = values();    //获取所有枚举集合
        for (SettingEnum item : enums) {
            if (item.getKey().equals(key)) {
                return item.getClassName();
            }
        }
        return null;
    }

    //根据传入的key动态获取description
    public static String getDescriptionByKey(String key) {
        SettingEnum[] enums = values();    //获取所有枚举集合
        for (SettingEnum item : enums) {
            if (item.getKey().equals(key)) {
                return item.getDescription();
            }
        }
        return null;
    }
}
