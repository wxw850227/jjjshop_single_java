package net.jjjshop.common.settings.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;
import net.jjjshop.common.enums.DeliveryTypeEnum;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel("系统设置VO")
public class StoreVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private List<Integer> deliveryType;
    private Boolean isGetLog;
    private KuaiDi100 kuaiDi100;
    private Policy policy;

    @Data
    @Accessors(chain = true)
    @ApiModel("快递100VO")
    public static class KuaiDi100 implements Serializable{
        private static final long serialVersionUID = 1L;
        private String customer;
        private String key;
        public KuaiDi100(){
            this.customer = "";
            this.key = "";
        }
    }

    @Data
    @Accessors(chain = true)
    @ApiModel("隐私政策")
    public static class Policy implements Serializable{
        private static final long serialVersionUID = 1L;
        private String service;
        private String privacy;
        public Policy(){
            this.service = "";
            this.privacy = "";
        }
    }

    public StoreVo(){
        this.name = "玖玖珈商城";
        this.deliveryType = DeliveryTypeEnum.getValues();
        this.isGetLog = false;
        this.kuaiDi100 = new KuaiDi100();
        this.policy = new Policy();
    }
}
