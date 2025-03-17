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
 * 公告组
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel("notice")
public class Notice implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("diyItem")
    private DiyItem item;

    public Notice(String imagePath){
        this.item = new DiyItem();
        item.setName("公告组");
        item.setType("notice");
        item.setGroup("media");
        // 参数
        JSONObject params = new JSONObject();
        params.put("text", "这里是第一条自定义公告的标题");
        params.put("icon", imagePath + "image/diy/notice.png");
        item.setParams(params);

        // 样式
        JSONObject style = new JSONObject();
        style.put("padding", 4);
        style.put("paddingTop", 10);
        style.put("paddingBottom", 0);
        style.put("paddingLeft", 10);
        style.put("topRadio", 5);
        style.put("bottomRadio", 5);
        style.put("bgcolor", "");
        style.put("background", "#ffffff");
        style.put("textColor", "#000000");
        item.setStyle(style);

        // 默认数据
        JSONArray data = new JSONArray();
        item.setData(data);
    }
}
