package net.jjjshop.shop.service.product.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.jjjshop.common.cache.ProductCategoryCache;
import net.jjjshop.common.entity.product.Product;
import net.jjjshop.common.entity.product.ProductImage;
import net.jjjshop.common.entity.product.ProductSku;
import net.jjjshop.common.entity.settings.Delivery;
import net.jjjshop.common.entity.shop.ProductSpecRel;
import net.jjjshop.common.mapper.product.ProductMapper;
import net.jjjshop.common.util.ProductUtils;
import net.jjjshop.common.util.UploadFileUtils;
import net.jjjshop.common.util.UserUtils;
import net.jjjshop.common.vo.product.ProductSkuVo;
import net.jjjshop.framework.common.service.impl.BaseServiceImpl;
import net.jjjshop.framework.core.pagination.PageInfo;
import net.jjjshop.framework.core.pagination.Paging;
import net.jjjshop.shop.param.product.ProductPageParam;
import net.jjjshop.shop.param.product.ProductParam;
import net.jjjshop.shop.service.product.*;
import net.jjjshop.shop.service.settings.DeliveryService;
import net.jjjshop.shop.service.user.UserGradeService;
import net.jjjshop.shop.vo.product.ProductVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品记录表 服务实现类
 * @author jjjshop
 * @since 2022-06-28
 */
@Slf4j
@Service
public class ProductServiceImpl extends BaseServiceImpl<ProductMapper, Product> implements ProductService {

    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private DeliveryService deliveryService;
    @Autowired
    private UserGradeService userGradeService;
    @Autowired
    private ProductSkuService productSkuService;
    @Autowired
    private ProductImageService productImageService;
    @Autowired
    private UploadFileUtils uploadFileUtils;
    @Autowired
    private ProductUtils productUtils;
    @Autowired
    private ProductSpecRelService productSpecRelService;
    @Autowired
    private ProductCategoryCache productCategoryCache;
    @Autowired
    private UserUtils userUtils;
    /**
     * 商品列表
     * @param productPageParam
     * @return
     */
    public Map<String,Object> getList(ProductPageParam productPageParam){
        Map<String,Object> result = new HashMap<>();
        // 商品列表
        Page<Product> page = new PageInfo<>(productPageParam);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getIsDelete, 0);
        //名称搜索
        if(StringUtils.isNotEmpty(productPageParam.getProductName())){
            wrapper.like(Product::getProductName, productPageParam.getProductName());
        }
        // 类型
        if(StringUtils.isNotEmpty(productPageParam.getType())){
            this.setWrapperByType(wrapper, productPageParam.getType());
        }
        // 分类
        if (productPageParam.getCategoryId() != null && productPageParam.getCategoryId() > 0) {
            List<Integer> subCategoryIds = productCategoryCache.getSubCategoryId(productPageParam.getCategoryId());
            wrapper.in(Product::getCategoryId, subCategoryIds);
        }
        IPage<Product> iPage = this.page(page, wrapper);
        // 最终返回分页对象
        IPage<ProductVo> resultPage = iPage.convert(item -> {
            ProductVo vo = new ProductVo();
            BeanUtil.copyProperties(item, vo);
            vo.setImagePath(this.getImagePath(vo.getProductId()));
            vo.setCategoryName(productCategoryService.getById(vo.getCategoryId()).getName());
            return vo;
        });
        result.put("productList", new Paging(resultPage));
        // 商品统计数量
        JSONObject productCount = new JSONObject();
        //出售中
        productCount.put("sell", this.getCount("sell"));
        //仓库中
        productCount.put("lower", this.getCount("lower"));
        //回收站
        productCount.put("recovery", this.getCount("recovery"));
        //库存紧张
        productCount.put("stock", this.getCount("stock"));
        //已售罄
        productCount.put("over", this.getCount("over"));
        result.put("productCount", productCount);
        // 商品分类
        result.put("categoryList", productCategoryCache.getCache());
        return result;
    }

    /**
     * 获取数量
     * @param type
     * @return
     */
    private Integer getCount(String type){
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        this.setWrapperByType(wrapper, type);
        wrapper.eq(Product::getIsDelete, 0);
        return this.count(wrapper);
    }

    /**
     * 根据类型设置查询条件
     * @param wrapper
     * @param type
     * @return
     */
    private void setWrapperByType(LambdaQueryWrapper<Product> wrapper, String type){
        // 销售中
        if (type.equalsIgnoreCase("sell")) {
            wrapper.eq(Product::getProductStatus, 10);
        }
        //仓库中
        if (type.equalsIgnoreCase("lower")) {
            wrapper.eq(Product::getProductStatus, 20);
        }
        // 回收站
        if (type.equalsIgnoreCase("recovery")) {
            wrapper.eq(Product::getProductStatus, 30);
        }
        //库存紧张
        if (type.equalsIgnoreCase("stock")) {
            wrapper.ge(Product::getProductStock, 1).le(Product::getProductStock, 10)
                    .eq(Product::getProductStatus, 10);
        }
        //已售罄
        if (type.equalsIgnoreCase("over")) {
            wrapper.eq(Product::getProductStock, 0).eq(Product::getProductStatus, 10);
        }
    }

    /**
     * 通过商品id查询主图
     * @param productId
     * @return
     */
    public String getImagePath(Integer productId){
        List<ProductImage> imageList = productImageService.list(new LambdaQueryWrapper<ProductImage>()
                .eq(ProductImage::getImageType, 0)
                .eq(ProductImage::getProductId, productId).last("limit 1").orderByAsc(ProductImage::getId));
        if(imageList.size() == 0){
            return "";
        }else{
            return uploadFileUtils.getFilePath(imageList.get(0).getImageId());
        }
    }

    /**
     * 新增商品
     * @param productParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean add(ProductParam productParam){
        Product product = new Product();
        BeanUtils.copyProperties(productParam, product);
        product.setAloneGradeEquity(StringEscapeUtils.unescapeHtml4(product.getAloneGradeEquity()));
        this.setProductParams(productParam, product);
        // 保存商品
        this.save(product);
        // 保存商品规格
        productParam.setProductId(product.getProductId());
        this.addProductSpec(productParam, false, null);
        // 保存商品图片
        this.addProductImages(productParam, product, 0);
        // 保存详情图片
        if(productParam.getIsPicture() == 1){
            this.addProductImages(productParam, product, 1);
        }
        return true;
    }

    /**
     * 添加商品规格
     * @param productParam
     * @param isUpdate
     * @param skuList
     * @return
     */
    private void addProductSpec(ProductParam productParam, boolean isUpdate, List<ProductSku> skuList)
    {
        // 更新模式: 先删除所有规格
        if(isUpdate){
            productSpecRelService.remove(new LambdaQueryWrapper<ProductSpecRel>()
                    .eq(ProductSpecRel::getProductId, productParam.getProductId()));
        }
        // 添加规格数据
        if (productParam.getSpecType() == 10) {
            // 单规格
            if(isUpdate){
                productSkuService.remove(new LambdaQueryWrapper<ProductSku>()
                        .eq(ProductSku::getProductId, productParam.getProductId())
                        .eq(ProductSku::getSpecSkuId, 0));
            }
            ProductSku sku = new ProductSku();
            BeanUtils.copyProperties(productParam.getSku(), sku);
            sku.setProductId(productParam.getProductId());
            productSkuService.save(sku);
        }else if (productParam.getSpecType() == 20) {
            // 添加商品与规格关系记录
            this.addProductSpecRel(productParam);
            // 添加商品sku
            this.addSkuList(productParam, skuList);
        }
    }

    /**
     * 多规格
     * @param productParam
     * @return
     */
    private void addProductSpecRel(ProductParam productParam){
        List<ProductSpecRel> list = new ArrayList<>();
        JSONArray specAttr = productParam.getSpecMany().getJSONArray("specAttr");
        for(int i=0;i<specAttr.size();i++){
            JSONArray specItems= specAttr.getJSONObject(i).getJSONArray("specItems");
            for (int j=0;j<specItems.size();j++){
                ProductSpecRel rel = productSpecRelService.getOne(new LambdaQueryWrapper<ProductSpecRel>()
                        .eq(ProductSpecRel::getProductId, productParam.getProductId())
                        .eq(ProductSpecRel::getSpecId, specAttr.getJSONObject(i).getInteger("groupId"))
                        .eq(ProductSpecRel::getSpecValueId, specItems.getJSONObject(j).getInteger("itemId")));
                if(rel == null){
                    ProductSpecRel bean = new ProductSpecRel();
                    bean.setProductId(productParam.getProductId());
                    bean.setSpecId(specAttr.getJSONObject(i).getInteger("groupId"));
                    bean.setSpecValueId(specItems.getJSONObject(j).getInteger("itemId"));
                    list.add(bean);
                }
            }
        }
        if(list.size() > 0){
            productSpecRelService.saveBatch(list);
        }
    }

    /**
     * 新增sku
     * @param productParam
     * @param skuList
     * @return
     */
    private void addSkuList(ProductParam productParam, List<ProductSku> skuList){
        JSONArray specList = productParam.getSpecMany().getJSONArray("specList");
        List<ProductSku> updateData = new ArrayList<>();
        List<ProductSku> saveData = new ArrayList<>();
        for(int i=0;i<specList.size();i++){
            ProductSku sku = JSONObject.toJavaObject(specList.getJSONObject(i).getJSONObject("specForm"), ProductSku.class);
            sku.setSpecSkuId(specList.getJSONObject(i).getString("specSkuId"));
            sku.setProductId(productParam.getProductId());
            if(specList.getJSONObject(i).getInteger("productSkuId") > 0){
                for(int j=0;j<skuList.size();j++){
                    if(skuList.get(j).getProductSkuId().intValue() == specList.getJSONObject(i).getInteger("productSkuId").intValue()){
                        skuList.remove(j);
                        break;
                    }
                }
                sku.setProductSkuId(specList.getJSONObject(i).getInteger("productSkuId"));
                updateData.add(sku);
            } else{
                saveData.add(sku);
            }
        }
        if(updateData.size() > 0){
            productSkuService.updateBatchById(updateData);
        }
        if(saveData.size() > 0){
            productSkuService.saveBatch(saveData);
        }
        // 删除
        if(CollectionUtils.isNotEmpty(skuList)){
            List<Integer> idList = new ArrayList<>();
            skuList.forEach(item->{
                idList.add(item.getProductSkuId());
            });
            productSkuService.remove(new LambdaQueryWrapper<ProductSku>().in(ProductSku::getProductSkuId, idList));
        }
    }

    private void setProductParams(ProductParam productParam, Product product){
        if(StringUtils.isBlank(productParam.getContent())){
            product.setContent(" ");
        }
        if (productParam.getSpecType() == 10) {
            // 单规格
            product.setProductStock(productParam.getSku().getStockNum());
            product.setProductPrice(productParam.getSku().getProductPrice());
            product.setHighPrice(productParam.getSku().getProductPrice());
            product.setLinePrice(productParam.getSku().getLinePrice());
        }else if (productParam.getSpecType() == 20){
            // 多规格
            JSONArray specList = productParam.getSpecMany().getJSONArray("specList");
            JSONObject specForm = specList.getJSONObject(0).getJSONObject("specForm");
            BigDecimal productPrice = specForm.getBigDecimal("productPrice");
            BigDecimal highPrice = specForm.getBigDecimal("productPrice");
            BigDecimal linePrice = specForm.getBigDecimal("linePrice");
            Integer stock = 0;
            // 取最低价
            for(int i=0;i<specList.size();i++){
                JSONObject item = specList.getJSONObject(i).getJSONObject("specForm");
                stock += item.getInteger("stockNum");
                if(item.getBigDecimal("productPrice").compareTo(productPrice) < 0){
                    productPrice = item.getBigDecimal("productPrice");
                    linePrice = item.getBigDecimal("linePrice");
                }
                if(item.getBigDecimal("productPrice").compareTo(highPrice) > 0){
                    highPrice = item.getBigDecimal("productPrice");
                }
            }
            product.setProductStock(stock);
            product.setProductPrice(productPrice);
            product.setHighPrice(highPrice);
            product.setLinePrice(linePrice);
        }
    }

    /**
     * 添加商品图片
     * imageType 0商品主图，1详情图
     * @param productParam
     * @param product
     * @param imageType
     * @return
     */
    private void addProductImages(ProductParam productParam, Product product, Integer imageType)
    {
        // 先删除图片
        productImageService.remove(new LambdaQueryWrapper<ProductImage>()
                .eq(ProductImage::getProductId, product.getProductId())
                .eq(ProductImage::getImageType, imageType));
        List<ProductImage> list = new ArrayList<>();
        List<ProductParam.ImageParam> imageList = null;
        if(imageType == 0){
            imageList = productParam.getImage();
        }else{
            imageList = productParam.getContentImage();
        }
        imageList.forEach(item -> {
            ProductImage image = new ProductImage();
            image.setImageId(item.getImageId());
            image.setProductId(product.getProductId());
            image.setImageType(imageType);
            list.add(image);
        });
        productImageService.saveBatch(list);
    }

    /**
     * 获取新增或修改数据
     * @param productId
     * @param scene
     * @return
     */
    public Map<String, Object> getBaseData(Integer productId, String scene){
        Map<String, Object> result = new HashMap<>();
        Product product = null;
        List<ProductSkuVo> skuVoList = null;
        if(productId > 0){
            product = this.getById(productId);
            // 商品信息
            ProductVo vo = new ProductVo();
            BeanUtils.copyProperties(product, vo);
            vo.setAloneGradeEquityJson(JSON.parseObject(vo.getAloneGradeEquity()));
            // sku
            vo.setSku(productUtils.getSkuByProductId(productId));
            skuVoList = vo.getSku();
            // image
            vo.setImage(productUtils.getListByProductId(productId, 0));
            // 详情图片
            if(product.getIsPicture() == 0){
                vo.setContentImage(new ArrayList<>());
            }else{
                vo.setContentImage(productUtils.getListByProductId(productId, 1));
            }
            // 视频地址
            vo.setVideoFilePath(uploadFileUtils.getFilePath(vo.getVideoId()));
            // 视频封面
            vo.setPosterFilePath(uploadFileUtils.getFilePath(vo.getPosterId()));
            result.put("model", vo);
        }
        // 所有分类
        result.put("category", productCategoryCache.getCache());
        // 配送模板
        result.put("delivery", deliveryService.list(new LambdaQueryWrapper<Delivery>().orderByAsc(Delivery::getSort)));
        // 会员等级
        result.put("gradeList", userUtils.getUsableGradeList(null));
        // 商品sku数据
        result.put("specData", productUtils.getSpecData(product, skuVoList));
        // 商品规格是否锁定
        result.put("isSpecLocked", false);
        return result;
    }

    /**
     * 修改商品
     * @param productParam
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean edit(ProductParam productParam){
        // 复制走新增流程
        if(productParam.getScene().equals("copy")){
            productParam.setProductId(0);
            return this.add(productParam);
        }
        Product product = new Product();
        BeanUtils.copyProperties(productParam, product);
        product.setAloneGradeEquity(StringEscapeUtils.unescapeHtml4(product.getAloneGradeEquity()));
        this.setProductParams(productParam, product);
        // 保存商品
        this.updateById(product);
        // 原sku
        List<ProductSku> skuList = productSkuService.list(new LambdaQueryWrapper<ProductSku>().eq(ProductSku::getProductId, productParam.getProductId()));
        // 保存商品规格
        this.addProductSpec(productParam, true, skuList);
        // 保存商品图片
        this.addProductImages(productParam, product, 0);
        // 保存详情图片
        if(productParam.getIsPicture() == 1){
            this.addProductImages(productParam, product, 1);
        }
        return true;
    }

    /**
     * 修改商品状态
     * @param productId
     * @param state
     * @return
     */
    public boolean setState(Integer productId, Integer state){
        return this.update(new LambdaUpdateWrapper<Product>().eq(Product::getProductId, productId)
                .set(Product::getProductStatus, state));
    }

    /**
     * 商品删除
     * @param productId
     * @return
     */
    public boolean setDelete(Integer productId){
        Product product = this.getById(productId);
        //  回收站，和未审核通过的直接删
        if(product.getProductStatus() == 30){
            return this.update(new LambdaUpdateWrapper<Product>().eq(Product::getProductId, productId)
                    .set(Product::getIsDelete, 1));
        } else{
            return this.update(new LambdaUpdateWrapper<Product>().eq(Product::getProductId, productId)
                    .set(Product::getProductStatus, 30));
        }
    }

    /**
     * 商品列表
     * @param productIds
     * @return
     */
    public List<ProductVo> getListByIds(List<Integer> productIds){
        List<Product> productList = this.list(new LambdaQueryWrapper<Product>()
                .in(Product::getProductId, productIds).eq(Product::getIsDelete, 0)
                .eq(Product::getProductStatus, 10));
        return productList.stream().map(e -> {
            ProductVo vo = new ProductVo();
            BeanUtil.copyProperties(e, vo);
            vo.setImagePath(this.getImagePath(vo.getProductId()));
            vo.setCategoryName(productCategoryService.getById(vo.getCategoryId()).getName());
            return vo;
        }).collect(Collectors.toList());
    }
}
