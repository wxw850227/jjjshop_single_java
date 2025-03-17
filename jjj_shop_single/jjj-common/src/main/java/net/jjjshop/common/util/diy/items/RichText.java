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
 * 富文本
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("richText")
public class RichText implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("diyItem")
    private DiyItem item;

    public RichText(){
        this.item = new DiyItem();
        item.setName("富文本");
        item.setType("richText");
        item.setGroup("tools");
        // 样式
        JSONObject style = new JSONObject();
        style.put("paddingTop", 0);
        style.put("paddingLeft", 0);
        style.put("background", "#ffffff");
        item.setStyle(style);

        // 参数
        JSONObject params = new JSONObject();
        params.put("content", "<p>这里是文本的内容</p>");
        item.setParams(params);

        // 默认数据
        JSONArray data = new JSONArray();
        item.setData(data);
    }
}
