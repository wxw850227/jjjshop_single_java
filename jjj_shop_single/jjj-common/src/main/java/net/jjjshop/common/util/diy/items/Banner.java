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
 * 轮播图组件
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("banner")
public class Banner implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("diyItem")
    private DiyItem item;

    public Banner(){
        this.item = new DiyItem();
        item.setName("图片轮播");
        item.setType("banner");
        item.setGroup("media");
        // 样式
        JSONObject style = new JSONObject();
        style.put("paddingTop", 0);
        style.put("paddingBottom", 0);
        style.put("paddingLeft", 0);
        style.put("topRadio", 0);
        style.put("bottomRadio", 0);
        style.put("btnColor", "#0095FF");
        style.put("background", "#ffffff");
        style.put("btnShape", "round");
        style.put("height", 340);

        item.setStyle(style);

        // 参数
        JSONObject params = new JSONObject();
        params.put("interval", "2800");
        item.setParams(params);

        // 默认数据
        JSONArray data = new JSONArray();
        JSONObject itemData = new JSONObject();
        itemData.put("imgUrl", "https://qn-cdn.jjjshop.net/20231023140955777.png");
        itemData.put("linkUrl", "");
        // 加2条数据
        data.add(itemData);
        data.add(itemData);
        item.setData(data);
    }
}
