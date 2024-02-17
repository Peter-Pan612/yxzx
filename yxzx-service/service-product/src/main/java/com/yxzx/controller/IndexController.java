package com.yxzx.controller;

import com.yxzx.mapper.ProductSkuMapper;
import com.yxzx.model.entity.product.Category;
import com.yxzx.model.entity.product.ProductSku;
import com.yxzx.model.vo.common.Result;
import com.yxzx.model.vo.common.ResultCodeEnum;
import com.yxzx.model.vo.h5.IndexVo;
import com.yxzx.service.CategoryService;
import com.yxzx.service.ProductService;
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
 * @date: 2024/2/16 21:03
 */
@Tag(name = "首页接口管理")
@RestController
@RequestMapping("/api/product/index")
public class IndexController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public Result index() {
        //1 所有一级分类
        List<Category> categoryList = categoryService.selectOneCategory();

        //2 根据销量排序，获取前10条记录
        List<ProductSku> productSkuList = productService.selectProductSkuBySale();

        //3 封装数据到vo对象
        IndexVo indexVo = new IndexVo();
        indexVo.setCategoryList(categoryList);
        indexVo.setProductSkuList(productSkuList);

        return Result.build(indexVo, ResultCodeEnum.SUCCESS);
    }

}
