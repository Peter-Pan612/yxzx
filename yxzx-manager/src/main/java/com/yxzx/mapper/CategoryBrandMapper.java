package com.yxzx.mapper;

import com.yxzx.model.dto.product.CategoryBrandDto;
import com.yxzx.model.entity.product.Brand;
import com.yxzx.model.entity.product.CategoryBrand;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryBrandMapper {
    List<CategoryBrand> findByPage(CategoryBrandDto categoryBrandDto);

    void save(CategoryBrand categoryBrand);

    List<Brand> findBrandByCategoryId(Long categoryId);
}
