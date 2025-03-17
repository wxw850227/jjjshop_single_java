package net.jjjshop.common.factory.upload.impl;

import com.alibaba.fastjson.JSONObject;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.enums.SettingEnum;
import net.jjjshop.common.factory.upload.UploadFactoryService;
import net.jjjshop.common.settings.vo.StorageVo;
import net.jjjshop.common.util.SettingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件腾讯云上传类
 */
@Slf4j
@Service
public class QCloudUploadServiceImpl implements UploadFactoryService {
    @Lazy
    @Autowired
    private SettingUtils settingUtils;
    /**
     * 文件上传，返回文件路径
     * @return
     */
    public void upload(MultipartFile multipartFile, String saveFileName) throws Exception{

        // 获取配置
        JSONObject vo = settingUtils.getSetting(SettingEnum.STORAGE.getKey(), null);
        StorageVo storageVo = JSONObject.toJavaObject(vo, StorageVo.class);

        COSCredentials cred = new BasicCOSCredentials(storageVo.getQCloud().getSecretId(), storageVo.getQCloud().getSecretKey());
        Region region = new Region(storageVo.getQCloud().getRegion());
        ClientConfig clientConfig = new ClientConfig(region);
        COSClient cosClient = new COSClient(cred, clientConfig);

        PutObjectRequest putObjectRequest = new PutObjectRequest(storageVo.getQCloud().getBucket(), saveFileName, multipartFile.getInputStream(), null);
        cosClient.putObject(putObjectRequest);
    }
}
