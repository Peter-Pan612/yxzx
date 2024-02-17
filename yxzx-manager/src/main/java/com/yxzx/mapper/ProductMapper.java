package com.yxzx.mapper;

import com.yxzx.model.dto.product.ProductDto;
import com.yxzx.model.entity.product.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    List<Product> findByPage( ProductDto productDto);

    void save(Product product);

    Product findProductById(Long id);

    void updateById(Product product);

    void deleteById(Long id);
}
