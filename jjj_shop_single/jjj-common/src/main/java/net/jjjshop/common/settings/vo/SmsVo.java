package net.jjjshop.common.settings.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel("短信设置VO")
public class SmsVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String acceptPhone;
    private String templateCode;
    private AliyunSms aliyunSms;


    @Data
    @Accessors(chain = true)
    @ApiModel("阿里云短信VO")
    public static class AliyunSms implements Serializable {
        private static final long serialVersionUID = 1L;
        private String accessKeyId;
        private String accessKeySecret;
        private String sign;

        public AliyunSms() {
            this.accessKeyId = "123";
            this.accessKeySecret = "123";
            this.sign = "三勾商城";
        }
    }

    public SmsVo() {
        this.acceptPhone = "123456";
        this.templateCode = "123456";
        this.aliyunSms = new AliyunSms();
    }
}
