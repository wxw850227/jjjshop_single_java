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
 * 标题
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("title")
public class TitleMoRen implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("diyItem")
    private DiyItem item;

    public TitleMoRen(){
        this.item = new DiyItem();
        item.setName("标题");
        item.setType("title");
        item.setGroup("media");
        // 样式
        JSONObject style = new JSONObject();
        style.put("paddingTop", 10);
        style.put("paddingBottom", 0);
        style.put("paddingLeft", 10);
        style.put("topRadio", 8);
        style.put("bottomRadio", 0);
        style.put("bgcolor", "#F2f2f2");
        style.put("textSize", 18);
        style.put("weight", 800);
        style.put("isLine", 1);
        style.put("lineColor", "#FF0000");
        style.put("isSub", 0);
        style.put("subtextSize", 14);
        style.put("subtextColor", "#FF0000");
        style.put("subbackground", "#FFCCCC");
        style.put("isMore", 1);
        style.put("moretextColor", "#0095FF");
        style.put("background", "#FFFFFF");
        style.put("textColor", "#333333");
        style.put("type", 8);
        item.setStyle(style);

        // 参数
        JSONObject params = new JSONObject();
        params.put("title", "热销商品");
        params.put("subtitle", "副标题名称");
        params.put("moretitle", "更多");
        params.put("showIcon", "yes");
        params.put("icon", "");
        params.put("linkUrl", "");
        params.put("sublinkUrl", "");
        item.setParams(params);

        // 默认数据
        JSONArray data = new JSONArray();
        item.setData(data);
    }
}
