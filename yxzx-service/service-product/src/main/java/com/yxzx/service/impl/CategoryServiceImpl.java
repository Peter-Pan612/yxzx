package com.yxzx.service.impl;


import com.alibaba.fastjson.JSON;
import com.yxzx.mapper.CategoryMapper;
import com.yxzx.model.entity.product.Category;
import com.yxzx.service.CategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/16 21:07
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private CategoryMapper categoryMapper;


    //查询所有一级分类
    //category::all
    @Cacheable(value = "category",key = "'all'")
    @Override
    public List<Category> selectOneCategory() {
        //1 查询redis，是否有一级分类
        String categoryOneJson = redisTemplate.opsForValue().get("category:one");
        //2如果redis包含所有一级分类，直接返回
        if (StringUtils.hasText(categoryOneJson)) {
            //转换
            List<Category> existCategoryList = JSON.parseArray(categoryOneJson, Category.class);
            return existCategoryList;
        }

        //3 如果redis没有一级分类，查询数据库，把数据库查询内容返回，并且查询内容放到redis里面
        List<Category> categoryList = categoryMapper.selectOneCategory();
        //询内容放到redis里面
        redisTemplate.opsForValue().set("category:one", JSON.toJSONString(categoryList),
                7, TimeUnit.DAYS);

        return categoryList;
    }

    @Override
    public List<Category> findCategoryTree() {
        //1 查询所有分类 返回List集合
        List<Category> allCategoryList = categoryMapper.findAll();

        //2 遍历所有分类list集合，通过条件parentId=0得到所有一级分类
        List<Category> oneCategoryList = allCategoryList.stream()
                .filter(item -> item.getParentId().longValue() == 0)
                .collect(Collectors.toList());

        //3 遍历所有一级分类list集合，条件判断 id = parentid，得到一级下面二级分类
        oneCategoryList.forEach(oneCategory -> {
            List<Category> twoCategoryList = allCategoryList.stream().filter(item -> item.getParentId() == oneCategory.getId())
                    .collect(Collectors.toList());
            //把二级分类封装到一级分类里面
            oneCategory.setChildren(twoCategoryList);

            //4 遍历所有二级分类list集合，条件判断 id = parentid，得到一级下面三级分类
            twoCategoryList.forEach(twoCategory -> {
                List<Category> threeCategoryList = allCategoryList.stream().filter(item -> item.getParentId() == twoCategory.getId())
                        .collect(Collectors.toList());
                //把二级分类封装到一级分类里面
                twoCategory.setChildren(threeCategoryList);
            });
        });


        return oneCategoryList;
    }
}
