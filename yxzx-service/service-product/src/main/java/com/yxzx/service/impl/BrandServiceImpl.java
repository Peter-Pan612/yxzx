package com.yxzx.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yxzx.mapper.BrandMapper;
import com.yxzx.model.dto.h5.ProductSkuDto;
import com.yxzx.model.entity.product.Brand;
import com.yxzx.model.entity.product.ProductSku;
import com.yxzx.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/17 10:12
 */
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandMapper brandMapper;

    //获取所有品牌
    @Override
    public List<Brand> findAll() {

        return brandMapper.findAll();
    }


}
