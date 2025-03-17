package net.jjjshop.common.settings.vo;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("交易设置VO")
public class TradeVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer freightRule;
    //未支付订单自动关闭时间
    private Integer closeDays;
    //自动关闭时间类型,1=天,2=小时,3=分钟
    private Integer closeType;
    private Integer receiveDays;
    private Integer refundDays;

    public TradeVo() {
        this.freightRule = 10;
        this.closeDays = 3;
        this.closeType = 1;
        this.receiveDays = 7;
        this.refundDays = 7;
    }

}
