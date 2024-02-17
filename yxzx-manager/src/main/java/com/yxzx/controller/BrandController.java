package com.yxzx.controller;

import com.github.pagehelper.PageInfo;
import com.yxzx.commom.log.annotation.Log;
import com.yxzx.commom.log.enums.OperatorType;
import com.yxzx.model.entity.product.Brand;
import com.yxzx.model.vo.common.Result;
import com.yxzx.model.vo.common.ResultCodeEnum;
import com.yxzx.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/15 19:55
 */
@RestController
@RequestMapping("/admin/product/brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    //查询所有品牌
    @GetMapping("/findAll")
    public Result findAll() {
        List<Brand> list = brandService.findAll();
        return Result.build(list, ResultCodeEnum.SUCCESS);
    }

    //列表
    @Log(title = "品牌管理:列表", businessType = 0,operatorType = OperatorType.MANAGE)
    @GetMapping("/{page}/{limit}")
    public Result list(@PathVariable Integer page,
                       @PathVariable Integer limit) {
        PageInfo<Brand> pageInfo = brandService.findByPage(page, limit);
        return Result.build(pageInfo, ResultCodeEnum.SUCCESS);
    }

    //添加
    @PostMapping("/save")
    public Result save(@RequestBody Brand brand) {
        brandService.save(brand);
        return Result.build(null, ResultCodeEnum.SUCCESS);
    }

    //TODO 修改


    //TODO 删除

}
