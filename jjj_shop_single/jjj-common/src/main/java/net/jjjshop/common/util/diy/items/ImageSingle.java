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
 * 单图组组件
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("imageSingle")
public class ImageSingle implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("diyItem")
    private DiyItem item;

    public ImageSingle(){
        this.item = new DiyItem();
        item.setName("单图组");
        item.setType("imageSingle");
        item.setGroup("media");
        // 样式
        JSONObject style = new JSONObject();
        style.put("background", "#ffffff");
        style.put("paddingTop", 0);
        style.put("paddingLeft", 0);
        item.setStyle(style);

        // 参数
        JSONObject params = new JSONObject();
        item.setParams(params);

        // 默认数据
        JSONArray data = new JSONArray();
        JSONObject itemData = new JSONObject();
        itemData.put("imgUrl", "");
        itemData.put("imgName", "banner.png");
        itemData.put("linkUrl", "");
        // 加2条数据
        data.add(itemData);
        data.add(itemData);
        item.setData(data);
    }
}
