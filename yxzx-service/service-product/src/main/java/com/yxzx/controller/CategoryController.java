package com.yxzx.controller;

import com.yxzx.model.entity.product.Category;
import com.yxzx.model.vo.common.Result;
import com.yxzx.model.vo.common.ResultCodeEnum;
import com.yxzx.service.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/16 21:42
 */
@Tag(name = "分类接口管理")
@RestController
@RequestMapping(value = "/api/product/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //查询所有分类，树形封装
    @GetMapping("/findCategoryTree")
    public Result findCategoryTree() {
        List<Category> list = categoryService.findCategoryTree();

        return Result.build(list, ResultCodeEnum.SUCCESS);
    }

}
