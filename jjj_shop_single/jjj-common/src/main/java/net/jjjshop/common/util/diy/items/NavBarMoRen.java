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
 * 导航组组件
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("navBar")
public class NavBarMoRen implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("diyItem")
    private DiyItem item;

    public NavBarMoRen(String imagePath){
        this.item = new DiyItem();
        item.setName("导航组");
        item.setType("navBar");
        item.setGroup("media");
        // 样式
        JSONObject style = new JSONObject();
        style.put("background", "#ffffff");
        style.put("rowsNum", 5);
        style.put("bgcolor", "#f2f2f2");
        style.put("paddingTop", 10);
        style.put("paddingBottom", 0);
        style.put("paddingLeft", 10);
        style.put("topRadio", 5);
        style.put("bottomRadio", 5);
        item.setStyle(style);

        // 参数
        JSONObject params = new JSONObject();
        item.setParams(params);

        // 加2条数据
        JSONArray data = new JSONArray();
        JSONObject itemData1 = new JSONObject();
        itemData1.put("color", "#666666");
        itemData1.put("imgName", "icon-1.png");
        itemData1.put("imgUrl", "https://qn-cdn.jjjshop.net/20231023142300740.png");
        itemData1.put("linkUrl", "pages/user/index/index");
        itemData1.put("name", "链接到 我的详情");
        itemData1.put("text", "我的详情");
        data.add(itemData1);
        JSONObject itemData2 = new JSONObject();
        itemData2.put("color", "#666666");
        itemData2.put("imgName", "icon-2.png");
        itemData2.put("imgUrl", "https://qn-cdn.jjjshop.net/20231023142311818.png");
        itemData2.put("linkUrl", "pages/order/myorder");
        itemData2.put("name", "链接到 我的订单");
        itemData2.put("text", "我的订单");
        data.add(itemData2);
        item.setData(data);
    }
}
