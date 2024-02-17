package com.yxzx.service;

import com.github.pagehelper.PageInfo;
import com.yxzx.model.dto.product.CategoryBrandDto;
import com.yxzx.model.entity.product.Brand;
import com.yxzx.model.entity.product.CategoryBrand;

import java.util.List;

public interface CategoryBrandService {
    PageInfo<CategoryBrand> findByPage(Integer page, Integer limit, CategoryBrandDto categoryBrandDto);

    void save(CategoryBrand categoryBrand);

    List<Brand> findBrandByCategoryId(Long categoryId);
}
