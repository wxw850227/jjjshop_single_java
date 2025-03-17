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
@ApiModel("video")
public class Video implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("diyItem")
    private DiyItem item;

    public Video(String imagePath){
        this.item = new DiyItem();
        item.setName("视频组");
        item.setType("video");
        item.setGroup("media");
        // 样式
        JSONObject style = new JSONObject();
        style.put("paddingTop", 0);
        style.put("height", 100);
        item.setStyle(style);

        // 参数
        JSONObject params = new JSONObject();
        params.put("videoUrl", "http://wxsnsdy.tc.qq.com/105/20210/snsdyvideodownload?filekey=30280201010421301f0201690402534804102ca905ce620b1241b726bc41dcff44e00204012882540400");
        params.put("poster", imagePath + "image/diy/video_poster.png");
        params.put("autoplay", 0);
        item.setParams(params);

        // 默认数据
        JSONArray data = new JSONArray();
        item.setData(data);
    }
}
