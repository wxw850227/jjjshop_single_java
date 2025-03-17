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
@ApiModel("service")
public class Service implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("diyItem")
    private DiyItem item;

    public Service(String imagePath){
        this.item = new DiyItem();
        item.setName("在线客服");
        item.setType("service");
        item.setGroup("tools");
        // 样式
        JSONObject style = new JSONObject();
        style.put("right", "1");
        style.put("bottom", "10");
        style.put("opacity", "100");
        item.setStyle(style);

        // 参数
        JSONObject params = new JSONObject();
        params.put("type", "chat");// '客服类型' => chat在线聊天，phone拨打电话
        params.put("image", imagePath + "image/diy/service.png");
        params.put("phoneNum", "");
        item.setParams(params);

        // 默认数据
        JSONArray data = new JSONArray();
        item.setData(data);
    }
}
