package com.yxzx.service;

import com.github.pagehelper.PageInfo;
import com.yxzx.model.entity.product.Brand;

import java.util.List;

public interface BrandService {
    PageInfo<Brand> findByPage(Integer page, Integer limit);

    void save(Brand brand);

    List<Brand> findAll();
}
