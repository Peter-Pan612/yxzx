package com.yxzx.service;

import com.yxzx.model.entity.product.Category;

import java.util.List;

public interface CategoryService {
    List<Category> selectOneCategory();

    List<Category> findCategoryTree();
}
