package net.jjjshop.front.service.page.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.page.Page;
import net.jjjshop.common.entity.plus.coupon.Coupon;
import net.jjjshop.common.entity.user.User;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.mapper.page.PageMapper;
import net.jjjshop.common.settings.vo.HomePushVo;
import net.jjjshop.common.util.PageUtils;
import net.jjjshop.common.util.SettingUtils;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.front.param.product.ProductParam;
import net.jjjshop.front.service.page.PageService;
import net.jjjshop.front.service.plus.article.ArticleService;
import net.jjjshop.front.service.plus.coupon.CouponService;
import net.jjjshop.front.service.product.ProductService;
import net.jjjshop.front.service.store.StoreService;
import net.jjjshop.front.vo.coupon.CouponListVo;
import net.jjjshop.front.vo.coupon.HomeCouponVo;
import net.jjjshop.front.vo.plus.article.ArticleListVo;
import net.jjjshop.front.vo.product.ProductListVo;
import net.jjjshop.front.vo.store.StoreVo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * diy页面表 服务实现类
 * @author jjjshop
 * @since 2022-07-28
 */
@Slf4j
@Service
public class PageServiceImpl extends BaseServiceImpl<PageMapper, Page> implements PageService {

    @Autowired
    private ProductService productService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private SettingUtils settingUtils;

    /**
     * 默认首页
     * @param user
     * @param pageId
     * @return
     */
    public synchronized JSONObject getPageData(User user, Integer pageId){
        // 如果pageId为空，则查首页
        Page page = null;
        LambdaQueryWrapper<Page> wrapper = new LambdaQueryWrapper<Page>()
                //页面类型(10首页 20自定义页 30个人中心)
                .eq(Page::getPageType, 10)
                .eq(Page::getIsDelete, 0)
                .eq(Page::getIsDefault, true);
        if(pageId == null){
            page = this.getOne(wrapper);
            if(page == null){
                //生成默认页
                page = PageUtils.getPage(10);
                int num  = this.count(new LambdaQueryWrapper<Page>()
                        .eq(Page::getPageType, 10)
                        .eq(Page::getIsDelete, 0)
                        .eq(Page::getIsDefault, true));
                //防止重复
                if(num == 0){
                    this.save(page);
                }
            }
        }else{
            page = this.getOne(new LambdaQueryWrapper<Page>().eq(Page::getPageId, pageId));
        }
        JSONObject pageData = JSON.parseObject(page.getPageData());
        JSONArray items = pageData.getJSONArray("items");
        for(int i=0;i<items.size();i++){
            JSONObject item = items.getJSONObject(i);
            if("product".equals(item.getString("type"))){
                // 商品
                item.put("data", this.getProductList(user, item));
            } else if("coupon".equals(item.getString("type"))){
                // 优惠券
                item.put("data", this.getCouponList(user, item));
            } else if("article".equals(item.getString("type"))){
                // 文章
                item.put("data", this.getArticleList(item));
            } else if("special".equals(item.getString("type"))){
                // 头条快报
                item.put("data", this.getSpecialList(item));
            } else if("store".equals(item.getString("type"))){
                // 门店
                item.put("data", this.getStoreList(item));
            }
        }
        return pageData;
    }

    /**
     * 获取商品信息
     * @param user
     * @param item
     * @return
     */
    private List<ProductListVo> getProductList(User user, JSONObject item){
        List<ProductListVo> list = null;
        // 获取商品数据
        if("choice".equals(item.getJSONObject("params").getString("source"))){
            // 数据来源：手动
            JSONArray array = item.getJSONArray("data");
            List<Integer> productIds = new ArrayList<>();
            for(int i=0;i<array.size();i++){
                Integer productId = array.getJSONObject(i).getInteger("productId");
                if(productId != null && productId > 0){
                    productIds.add(productId);
                }
            }
            if(productIds.size() > 0){
                list = productService.getListByProductIds(productIds, user);
            }
        }else{
            // 数据来源：自动
            ProductParam params = new ProductParam();
            params.setPageIndex(1L);
            params.setPageSize(item.getJSONObject("params").getJSONObject("auto").getLong("showNum"));
            Paging<ProductListVo> pageList = productService.getList(params, user);
            list = pageList.getRecords();
        }
        return list;
    }

    /**
     * 优惠券
     * @param user
     * @param item
     * @return
     */
    private List<CouponListVo> getCouponList(User user, JSONObject item){
        if(user == null){
            return new ArrayList<>();
        }else {
            return couponService.getWaitList(user, item.getJSONObject("params").getInteger("limit"));
        }
    }

    /**
     * 文章
     * @param item
     * @return
     */
    private List<ArticleListVo> getArticleList(JSONObject item){
        Integer pageSize = item.getJSONObject("params").getJSONObject("auto").getInteger("showNum");
        Integer category = item.getJSONObject("params").getJSONObject("auto").getInteger("category");
        Paging<ArticleListVo> page = articleService.getList(1, pageSize, category);
        return page.getRecords();
    }

    /**
     * 头条快报
     * @param item
     * @return
     */
    private List<ArticleListVo> getSpecialList(JSONObject item){
        Integer pageSize = item.getJSONObject("params").getJSONObject("auto").getInteger("showNum");
        Integer category = item.getJSONObject("params").getJSONObject("auto").getInteger("category");
        Paging<ArticleListVo> page = articleService.getList(1, pageSize, category);
        return page.getRecords();
    }

    /**
     * 门店
     * @param item
     * @return
     */
    private List<StoreVo> getStoreList(JSONObject item){
        List<StoreVo> list = null;
        // 获取商品数据
        if("choice".equals(item.getJSONObject("params").getString("source"))){
            // 数据来源：手动
            JSONArray array = item.getJSONArray("data");
            List<Integer> storeIds = new ArrayList<>();
            for(int i=0;i<array.size();i++){
                Integer storeId = array.getJSONObject(i).getInteger("storeId");
                if(storeId != null && storeId > 0){
                    storeIds.add(storeId);
                }
            }
            if(storeIds.size() > 0){
                list = storeService.getListByStoreIds(storeIds);
            }
        }else{
            // 数据来源：自动
            list = storeService.getList(item.getJSONObject("params").getJSONObject("auto").getInteger("showNum"));
        }
        return list;
    }

    /**
     * 获取首页推送优惠券
     * @param
     * @return
     */
    public JSONObject getHomePush(){
        JSONObject setting = settingUtils.getSetting(SettingEnum.HOMEPUSH.getKey(), null);
        HomePushVo homePushVo = setting.toJavaObject(HomePushVo.class);
        if ("3".equals(homePushVo.getType())) {
            List<JSONObject> coupons = setting.getObject("coupon", List.class);
            List<Integer> couponIds = coupons.stream().map(e -> {
                return e.getInteger("couponId");
            }).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(couponIds)){
                List<Coupon> couponList = couponService.list(new LambdaQueryWrapper<Coupon>()
                        .eq(Coupon::getIsDelete, 0)
                        .in(Coupon::getCouponId, couponIds));
                List<HomeCouponVo> homeCouponVos = couponList.stream().map(e -> {
                    HomeCouponVo homeCouponVo = new HomeCouponVo();
                    BeanUtils.copyProperties(e, homeCouponVo);
                    return homeCouponVo;
                }).collect(Collectors.toList());
                setting.fluentPut("coupon", homeCouponVos);
            }
            return setting;
        } else {
            return setting;
        }
    }
}
