package net.jjjshop.common.util.diy.items;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import net.jjjshop.common.util.diy.DiyItem;

/**
 * 优惠券组
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("coupon")
public class Coupon implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("diyItem")
    private DiyItem item;

    public Coupon(String imagePath){
        this.item = new DiyItem();
        item.setName("优惠券组");
        item.setType("coupon");
        item.setGroup("shop");
        // 样式
        JSONObject style = new JSONObject();
        style.put("paddingTop", 10);
        style.put("paddingBottom", 10);
        style.put("paddingLeft", 10);
        style.put("topRadio", 5);
        style.put("bottomRadio", 5);
        style.put("bgcolor", "#f2f2f2");
        style.put("background", "#0095FF");
        style.put("descolor", "#666666");
        style.put("pricecolor", "#0095FF");
        style.put("cillcolor", "#0095FF");
        style.put("btncolor", "#0095FF");
        style.put("btnTxtcolor", "#ffffff");
        style.put("btnRadio", 24);
        style.put("bgtype", 1);
        style.put("bgimage", imagePath + "image/diy/active/coupon.png");
        item.setStyle(style);

        // 参数
        JSONObject params = new JSONObject();
        params.put("btntext", "立即领取");
        params.put("limit", 5);
        item.setParams(params);

        // 默认数据
        JSONArray data = new JSONArray();
        JSONObject itemData = new JSONObject();
        itemData.put("color", "red");
        itemData.put("reducePrice", "10");
        itemData.put("minPrice", "100.00");
        // 2条数据
        data.add(itemData);
        data.add(itemData);
        item.setData(data);
    }
}
