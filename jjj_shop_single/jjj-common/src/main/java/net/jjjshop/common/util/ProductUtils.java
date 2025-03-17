package net.jjjshop.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.entity.product.ProductImage;
import net.jjjshop.common.entity.product.ProductSku;
import net.jjjshop.common.entity.shop.ProductSpecRel;
import net.jjjshop.common.service.product.*;
import net.jjjshop.common.vo.product.ProductImageVo;
import net.jjjshop.common.vo.product.ProductSkuVo;
import net.jjjshop.common.vo.product.ProductSpecRelVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProductUtils {
    @Autowired
    private UploadFileUtils uploadFileUtils;
    @Autowired
    private ProductSkuService productSkuService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private ProductSpecRelService productSpecRelService;
    @Autowired
    private ProductSpecService productSpecService;
    @Autowired
    private ProductSpecValueService productSpecValueService;
    /**
     * 商品规格数据
     * @param product
     * @return
     */
    public Map<String, Object> getSpecData(Product product, List<ProductSkuVo> skuList){
        if(product == null || product.getSpecType() != 20){
            return null;
        }
        Map<String, Object> result = new HashMap<>();
        // specRel
        List<ProductSpecRelVo> specList = this.getSpecByProductId(product.getProductId());
        Map<Integer,JSONObject> specAttrData = new HashMap<>();
        specList.forEach(item ->{
            if(specAttrData.get(item.getSpecId()) == null){
                JSONObject spec = new JSONObject();
                spec.put("groupId", item.getSpecId());
                spec.put("groupName", item.getSpecName());
                spec.put("specItems", new JSONArray());
                specAttrData.put(item.getSpecId(), spec);
            }
            JSONObject specItem = new JSONObject();
            specItem.put("itemId", item.getSpecValueId());
            specItem.put("specValue", item.getSpecValue());
            specAttrData.get(item.getSpecId()).getJSONArray("specItems").add(specItem);
        });
        List<JSONObject> specListData = new ArrayList<>();
        skuList.forEach(item ->{
            JSONObject sku = new JSONObject();
            sku.put("productSkuId", item.getProductSkuId());
            sku.put("specSkuId", item.getSpecSkuId());
            sku.put("rows", new JSONArray());
            JSONObject specForm = new JSONObject();
            specForm.put("imageId", item.getImageId());
            specForm.put("imagePath", item.getImagePath());
            specForm.put("productNo", item.getProductNo());
            specForm.put("productPrice", item.getProductPrice());
            specForm.put("productWeight", item.getProductWeight());
            specForm.put("linePrice", item.getLinePrice());
            specForm.put("stockNum", item.getStockNum());
            sku.put("specForm", specForm);
            specListData.add(sku);
        });
        result.put("specAttr", specAttrData.values().stream().collect(Collectors.toList()));
        result.put("specList", specListData);
        return result;
    }

    /**
     * 根据商品id查询sku
     * @param productId
     * @return
     */
    public List<ProductSkuVo> getSkuByProductId(Integer productId){
        List<ProductSku> skuList = productSkuService.list(new LambdaQueryWrapper<ProductSku>()
                .eq(ProductSku::getProductId, productId).orderByAsc(ProductSku::getProductSkuId));
        return skuList.stream().map(e -> {
            ProductSkuVo productSkuVo = new ProductSkuVo();
            BeanUtils.copyProperties(e, productSkuVo);
            productSkuVo.setImagePath(uploadFileUtils.getFilePath(e.getImageId()));
            return productSkuVo;
        }).collect(Collectors.toList());
    }

    /**
     * 根据商品id查询spec
     * @param productId
     * @return
     */
    public List<ProductSpecRelVo> getSpecByProductId(Integer productId){
        List<ProductSpecRel> specList = productSpecRelService.list(new LambdaQueryWrapper<ProductSpecRel>()
                .eq(ProductSpecRel::getProductId, productId).orderByAsc(ProductSpecRel::getId));
        return specList.stream().map(e -> {
            ProductSpecRelVo productSpecRelVo = new ProductSpecRelVo();
            BeanUtils.copyProperties(e, productSpecRelVo);
            productSpecRelVo.setSpecName(productSpecService.getById(e.getSpecId()).getSpecName());
            productSpecRelVo.setSpecValue(productSpecValueService.getById(e.getSpecValueId()).getSpecValue());
            return productSpecRelVo;
        }).collect(Collectors.toList());
    }

    /**
     * 根据id和图片类型获取图片
     * @param productId  商品id
     * @param imageType  图片类型
     * @return
     */
    public List<ProductImageVo> getListByProductId(Integer productId, Integer imageType){
        List<ProductImage> imageList = productImageService.list(new LambdaQueryWrapper<ProductImage>()
                .eq(ProductImage::getProductId, productId)
                .eq(ProductImage::getImageType, imageType).orderByAsc(ProductImage::getId));
        return imageList.stream().map(e -> {
            ProductImageVo productImageVo = new ProductImageVo();
            BeanUtils.copyProperties(e, productImageVo);
            productImageVo.setFilePath(uploadFileUtils.getFilePath(e.getImageId()));
            return productImageVo;
        }).collect(Collectors.toList());
    }

    /**
     * 获取规格名
     * @return
     */
    public String getProductAttr(Integer productId, String specSkuId, Integer specType){
        String productAttr = "";
        if (specType == 20) {
            List<ProductSpecRelVo> specRelVoList = this.getSpecByProductId(productId);
            String[] attrs = specSkuId.split("_");
            for(String attr:attrs){
                ProductSpecRelVo vo = this.getSpecRelVo(specRelVoList, Integer.valueOf(attr));
                if(vo != null){
                    productAttr += vo.getSpecName() + ":" + vo.getSpecValue() + ";";
                }
            }
        }
        return productAttr;
    }

    /**
     * 根据id获取specRelVo
     * @param specValueId
     * @return
     */
    private ProductSpecRelVo getSpecRelVo(List<ProductSpecRelVo> specRelVoList, Integer specValueId){
        ProductSpecRelVo result = null;
        for(ProductSpecRelVo vo:specRelVoList){
            if(vo.getSpecValueId().intValue() == specValueId.intValue()){
                result = vo;
                break;
            }
        }
        return result;
    }

    public ProductSkuVo getProductSku(Integer productId, Integer specType, String specSkuId){
        List<ProductSkuVo> skuList = this.getSkuByProductId(productId);
        // 获取指定的sku
        ProductSkuVo productSku = null;
        for(ProductSkuVo item:skuList){
            if(item.getSpecSkuId().equals(specSkuId)){
                productSku = item;
                break;
            }
        }
        if (productSku == null) {
            return null;
        }
        // 多规格文字内容
        productSku.setProductAttr(this.getProductAttr(skuList.get(0).getProductId(), productSku.getSpecSkuId(), specType));
        return productSku;
    }
}
