package com.yxzx.service;

import com.github.pagehelper.PageInfo;
import com.yxzx.model.dto.h5.ProductSkuDto;
import com.yxzx.model.entity.product.Brand;
import com.yxzx.model.entity.product.ProductSku;

import java.util.List;

public interface BrandService {
    List<Brand> findAll();


}
