package net.jjjshop.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.kuaidi100.sdk.api.QueryTrack;
import com.kuaidi100.sdk.core.IBaseClient;
import com.kuaidi100.sdk.pojo.HttpResult;
import com.kuaidi100.sdk.request.QueryTrackParam;
import com.kuaidi100.sdk.request.QueryTrackReq;
import com.kuaidi100.sdk.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.settings.vo.StoreVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KuaiDi100Utils {

    @Autowired
    private SettingUtils settingUtils;

    public JSONObject query(String expressCode, String expressNo) throws Exception {
        JSONObject store = settingUtils.getSetting(SettingEnum.STORE.getKey(), null);
        StoreVo storeVo = store.toJavaObject(StoreVo.class);
        String key = storeVo.getKuaiDi100().getKey();
        String customer = storeVo.getKuaiDi100().getCustomer();

        QueryTrackReq queryTrackReq = new QueryTrackReq();
        QueryTrackParam queryTrackParam = new QueryTrackParam();
        queryTrackParam.setCom(expressCode);
        queryTrackParam.setNum(expressNo);
        queryTrackParam.setResultv2("1");
        String param = new Gson().toJson(queryTrackParam);
        queryTrackReq.setParam(param);
        queryTrackReq.setCustomer(customer);
        queryTrackReq.setSign(SignUtils.querySign(param ,key,customer));

        IBaseClient baseClient = new QueryTrack();
        HttpResult execute = baseClient.execute(queryTrackReq);
        JSONObject result = (JSONObject)JSON.parse(execute.getBody());
        return result;
    }
}
