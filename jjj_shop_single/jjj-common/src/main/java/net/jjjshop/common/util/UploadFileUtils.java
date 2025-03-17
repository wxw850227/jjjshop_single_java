package net.jjjshop.common.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.file.UploadFile;
import net.jjjshop.common.entity.product.ProductImage;
import net.jjjshop.common.enums.StorageEnum;
import net.jjjshop.common.service.file.UploadFileService;
import net.jjjshop.common.service.product.ProductImageService;
import net.jjjshop.config.properties.SpringBootJjjProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class UploadFileUtils {

    @Autowired
    private UploadFileService uploadFileService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private SpringBootJjjProperties springBootJjjProperties;
    /**
     * 通过文件id查找文件路径
     * @param fileId
     * @return
     */
    public String getFilePath(Integer fileId){
        UploadFile file = uploadFileService.getOne(new LambdaQueryWrapper<UploadFile>()
                .eq(UploadFile::getFileId, fileId));
        return this.getFilePathByFile(file);
    }

    public String getFilePathByFile(UploadFile file){
        String filePath = "";
        if(file != null){
            if(StorageEnum.LOCAL.getValue().equals(file.getStorage())){
                filePath = springBootJjjProperties.getResourceAccessUrl()  + file.getFileName();
            }else{
                filePath = file.getFileUrl() + "/" + file.getFileName();
            }
        }
        return filePath;
    }

    /**
     * 通过商品id查询主图
     * @param productId
     * @return
     */
    public String getImagePathByProductId(Integer productId){
        List<ProductImage> imageList = productImageService.list(new LambdaQueryWrapper<ProductImage>()
                .eq(ProductImage::getImageType, 0)
                .eq(ProductImage::getProductId, productId).last("limit 1").orderByAsc(ProductImage::getId));
        if(imageList.size() == 0){
            return "";
        }else{
            return this.getFilePath(imageList.get(0).getImageId());
        }
    }

}
