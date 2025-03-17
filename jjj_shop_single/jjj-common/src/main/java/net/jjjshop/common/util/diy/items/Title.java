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
public class Title implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("diyItem")
    private DiyItem item;

    public Title(){
        this.item = new DiyItem();
        item.setName("标题");
        item.setType("title");
        item.setGroup("media");
        // 样式
        JSONObject style = new JSONObject();
        style.put("paddingTop", 0);
        style.put("background", "#f5f5f5");
        style.put("textColor", "#FF0000");
        item.setStyle(style);

        // 参数
        JSONObject params = new JSONObject();
        params.put("title", "标题名称");
        params.put("showIcon", "yes");
        params.put("icon", "");
        item.setParams(params);

        // 默认数据
        JSONArray data = new JSONArray();
        item.setData(data);
    }
}
