package com.yxzx.service;

import com.github.pagehelper.PageInfo;
import com.yxzx.model.dto.h5.ProductSkuDto;
import com.yxzx.model.entity.product.ProductSku;
import com.yxzx.model.vo.h5.ProductItemVo;

import java.util.List;

public interface ProductService {
    List<ProductSku> selectProductSkuBySale();

    PageInfo<ProductSku> findByPage(Integer page, Integer limit, ProductSkuDto productSkuDto);

    ProductItemVo item(Long skuId);

    ProductSku getBySkuId(Long skuId);
}
