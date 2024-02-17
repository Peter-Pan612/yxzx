package com.yxzx.controller;

import com.yxzx.model.entity.product.Category;
import com.yxzx.model.vo.common.Result;
import com.yxzx.model.vo.common.ResultCodeEnum;
import com.yxzx.service.CategoryService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/7 20:58
 */

@RestController
@RequestMapping("/admin/product/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //分类的列表，每次只查询一层数据
    @GetMapping("/findCategoryList/{id}")
    public Result findCategoryList(@PathVariable Long id){
       List<Category> list =  categoryService.findCategoryList(id);
       return Result.build(list, ResultCodeEnum.SUCCESS);

    }

    //导出
    @GetMapping("/exportData")
    public void exportData(HttpServletResponse response){
        categoryService.exportData(response);
    }

    //导入
    @PostMapping("/importData")
    public Result importData(MultipartFile file){
        //获取上传文件
        categoryService.importData(file);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

}
