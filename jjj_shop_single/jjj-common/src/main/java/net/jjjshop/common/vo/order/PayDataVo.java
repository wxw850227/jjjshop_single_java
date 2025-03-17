

package net.jjjshop.common.vo.order;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("支付成功数据VO")
public class PayDataVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("附属信息")
    private JSONObject attach;

    @ApiModelProperty("交易流水号")
    private String transaction_id;
}
