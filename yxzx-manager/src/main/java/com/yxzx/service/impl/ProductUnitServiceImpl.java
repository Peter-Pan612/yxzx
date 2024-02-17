package com.yxzx.service.impl;

import com.yxzx.mapper.ProductUnitMapper;
import com.yxzx.model.entity.base.ProductUnit;
import com.yxzx.service.ProductUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/15 21:36
 */
@Service
public class ProductUnitServiceImpl implements ProductUnitService {
    @Autowired
    private ProductUnitMapper productUnitMapper;

    @Override
    public List<ProductUnit> findAll() {
        return productUnitMapper.findAll();
    }
}
