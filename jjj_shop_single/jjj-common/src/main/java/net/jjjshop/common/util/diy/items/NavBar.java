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
public class NavBar implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("diyItem")
    private DiyItem item;

    public NavBar(String imagePath){
        this.item = new DiyItem();
        item.setName("导航组");
        item.setType("navBar");
        item.setGroup("media");
        // 样式
        JSONObject style = new JSONObject();
        style.put("background", "#ffffff");
        style.put("rowsNum", 4);
        item.setStyle(style);

        // 参数
        JSONObject params = new JSONObject();
        item.setParams(params);

        // 加4条数据
        JSONArray data = new JSONArray();
        JSONObject itemData1 = new JSONObject();
        itemData1.put("imgUrl", imagePath + "image/diy/navbar/01.png");
        itemData1.put("imgName", "icon-1.png");
        itemData1.put("linkUrl", "");
        itemData1.put("text", "按钮文字1");
        itemData1.put("color", "#666666");
        data.add(itemData1);
        JSONObject itemData2 = new JSONObject();
        itemData2.put("imgUrl", imagePath + "image/diy/navbar/02.png");
        itemData2.put("imgName", "icon-2.png");
        itemData2.put("linkUrl", "");
        itemData2.put("text", "按钮文字2");
        itemData2.put("color", "#666666");
        data.add(itemData2);
        JSONObject itemData3 = new JSONObject();
        itemData3.put("imgUrl", imagePath + "image/diy/navbar/03.png");
        itemData3.put("imgName", "icon-3.png");
        itemData3.put("linkUrl", "");
        itemData3.put("text", "按钮文字3");
        itemData3.put("color", "#666666");
        data.add(itemData3);
        JSONObject itemData4 = new JSONObject();
        itemData4.put("imgUrl", imagePath + "image/diy/navbar/04.png");
        itemData4.put("imgName", "icon-4.png");
        itemData4.put("linkUrl", "");
        itemData4.put("text", "按钮文字4");
        itemData4.put("color", "#666666");
        data.add(itemData4);
        item.setData(data);
    }
}
