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
 * 辅助线组件
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("guide")
public class Guide implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("diyItem")
    private DiyItem item;

    public Guide(){
        this.item = new DiyItem();
        item.setName("辅助线");
        item.setType("guide");
        item.setGroup("tools");
        // 样式
        JSONObject style = new JSONObject();
        style.put("background", "#ffffff");
        style.put("lineColor", "#000000");
        style.put("lineStyle", "solid");
        style.put("lineHeight", 1);
        style.put("paddingTop", 10);
        item.setStyle(style);

        // 参数
        JSONObject params = new JSONObject();
        item.setParams(params);

        // 默认数据
        JSONArray data = new JSONArray();
        item.setData(data);
    }
}
