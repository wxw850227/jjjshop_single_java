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
 * 头条快报
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("special")
public class Special implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("diyItem")
    private DiyItem item;

    public Special(String imagePath){
        this.item = new DiyItem();
        item.setName("头条快报");
        item.setType("special");
        item.setGroup("media");
        // 样式
        JSONObject style = new JSONObject();
        style.put("display", 1);
        style.put("image", imagePath + "image/diy/special.png");
        item.setStyle(style);

        // 参数
        JSONObject params = new JSONObject();
        params.put("source", "auto");
        JSONObject auto = new JSONObject();
        auto.put("category", 0);
        auto.put("showNum", 6);
        params.put("auto", auto);
        item.setParams(params);

        // 默认数据
        JSONArray data = new JSONArray();
        item.setData(data);

        // 默认数据
        JSONArray defaultData = new JSONArray();
        JSONObject itemData = new JSONObject();
        itemData.put("articleTitle", "此处显示头条快报标题");
        // 加1条数据
        defaultData.add(itemData);
        item.setDefaultData(defaultData);
    }
}
