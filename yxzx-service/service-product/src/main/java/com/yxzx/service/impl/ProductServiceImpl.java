package com.yxzx.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxzx.mapper.ProductDetailsMapper;
import com.yxzx.mapper.ProductMapper;
import com.yxzx.mapper.ProductSkuMapper;
import com.yxzx.model.dto.h5.ProductSkuDto;
import com.yxzx.model.entity.product.Product;
import com.yxzx.model.entity.product.ProductDetails;
import com.yxzx.model.entity.product.ProductSku;
import com.yxzx.model.vo.h5.ProductItemVo;
import com.yxzx.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/16 21:07
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductSkuMapper productSkuMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductDetailsMapper productDetailsMapper;


    @Override
    public List<ProductSku> selectProductSkuBySale() {

        return productSkuMapper.selectProductSkuSale();
    }

    @Override
    public PageInfo<ProductSku> findByPage(Integer page, Integer limit, ProductSkuDto productSkuDto) {
        PageHelper.startPage(page, limit);
        List<ProductSku> list = productSkuMapper.findByPage(productSkuDto);
        return new PageInfo<>(list);
    }

    @Override
    public ProductItemVo item(Long skuId) {
        //1 创建vo对象，用于封装数据
        ProductItemVo productItemVo = new ProductItemVo();
        //2 根据skuId获取sku信息
        ProductSku productSku = productSkuMapper.getById(skuId);

        //3 根据第二部获取sku,从sku获取productId，获取商品信息
        Long productId = productSku.getProductId();
        Product product = productMapper.getById(productId);

        //4 productId，获取商品详情信息
        ProductDetails productDetails = productDetailsMapper.getByProductId(productSku.getProductId());

        //5 封装map集合 == 商品规格对应商品的skuId信息
        Map<String, Object> skuSpecValueMap = new HashMap<>();
        //根据商品id获取商品所有sku列表
        List<ProductSku> productSkuList = productSkuMapper.findByProductId(productId);
        productSkuList.forEach(item -> {
            skuSpecValueMap.put(item.getSkuSpec(), item.getId());
        });

        //6 需要数据封装到productItemVo里面
        productItemVo.setProduct(product);
        productItemVo.setProductSku(productSku);
        productItemVo.setSkuSpecValueMap(skuSpecValueMap);

        //封装详情图片
        productItemVo.setDetailsImageUrlList(Arrays.asList(productDetails.getImageUrls().split(",")));

        //封装轮播图list集合
        productItemVo.setSliderUrlList(Arrays.asList(product.getSliderUrls().split(",")));

        //商品规格信息
        productItemVo.setSpecValueList(JSON.parseArray(product.getSpecValue()));

        return productItemVo;
    }


    //远程调用
    @Override
    public ProductSku getBySkuId(Long skuId) {
        ProductSku productSku = productSkuMapper.getById(skuId);

        return productSku;
    }
}
