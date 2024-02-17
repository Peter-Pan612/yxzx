package com.yxzx.mapper;

import com.yxzx.model.entity.product.Category;
import com.yxzx.model.vo.product.CategoryExcelVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category> selectCategoryByParentId(Long id);

    int selectCountByParentId(Long id);

    List<Category> findAll();


    void batcheInsert(List<CategoryExcelVo> categoryList);
}
