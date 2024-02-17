package com.yxzx.controller;

import com.github.pagehelper.PageInfo;
import com.yxzx.model.dto.h5.ProductSkuDto;
import com.yxzx.model.entity.product.Brand;
import com.yxzx.model.entity.product.ProductSku;
import com.yxzx.model.vo.common.Result;
import com.yxzx.model.vo.common.ResultCodeEnum;
import com.yxzx.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/17 10:10
 */

@Tag(name = "品牌管理")
@RestController
@RequestMapping(value = "/api/product/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @Operation(summary = "获取全部品牌")
    @GetMapping("findAll")
    public Result findAll() {
        List<Brand> list = brandService.findAll();
        return Result.build(list, ResultCodeEnum.SUCCESS);
    }


}
