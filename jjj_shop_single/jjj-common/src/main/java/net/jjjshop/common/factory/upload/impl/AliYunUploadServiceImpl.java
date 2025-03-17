package net.jjjshop.common.factory.upload.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
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
 * 文件七牛上传类
 */
@Slf4j
@Service
public class AliYunUploadServiceImpl implements UploadFactoryService {
    @Lazy
    @Autowired
    private SettingUtils settingUtils;
    /**
     * 阿里云OSS文件上传，返回文件路径
     * @return
     */
    public void upload(MultipartFile multipartFile, String saveFileName) throws Exception{
        // 获取配置
        JSONObject vo = settingUtils.getSetting(SettingEnum.STORAGE.getKey(), null);
        StorageVo storageVo = JSONObject.toJavaObject(vo, StorageVo.class);

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(storageVo.getAliYun().getEndpoint(),
                storageVo.getAliYun().getAccessKeyId(), storageVo.getAliYun().getAccessKeySecret());

        // 上传文件流。
        ossClient.putObject(storageVo.getAliYun().getBucket(), saveFileName, multipartFile.getInputStream());

        // 关闭OSSClient。
        ossClient.shutdown();
    }
}
