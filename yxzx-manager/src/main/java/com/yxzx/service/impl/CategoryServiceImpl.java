package com.yxzx.service.impl;


import com.alibaba.excel.EasyExcel;
import com.yxzx.exception.yxzxException;
import com.yxzx.listener.ExcelListener;
import com.yxzx.mapper.CategoryMapper;
import com.yxzx.model.entity.product.Category;
import com.yxzx.model.vo.common.ResultCodeEnum;
import com.yxzx.model.vo.product.CategoryExcelVo;
import com.yxzx.service.CategoryService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/7 20:59
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<Category> findCategoryList(Long id) {
        //1 根据id条件进行查询，返回list集合
        List<Category> categoryList = categoryMapper.selectCategoryByParentId(id);

        //2 遍历返回List集合，判断每个分类是否有下一层分类，如果有设置hasChildren = true
        if (!CollectionUtils.isEmpty(categoryList)) {
            categoryList.forEach(category -> {
                //判断每个分类是否有下一层分类
                int count = categoryMapper.selectCountByParentId(category.getId());
                if (count > 0) {
                    category.setHasChildren(true);
                } else {
                    category.setHasChildren(false);
                }
            });
        }


        return categoryList;
    }

    //导出
    @Override
    public void exportData(HttpServletResponse response) {

        try {
            //1 设置响应头信息
            // 设置响应结果类型
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");

            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("分类数据", "UTF-8");

            //设置响应头信息
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            //2 调用mapper方法查询所有分类，返回List集合
            List<Category> categoryList = categoryMapper.findAll();

            List<CategoryExcelVo> categoryExcelVoList = new ArrayList<>();
            for (Category category : categoryList) {
                CategoryExcelVo categoryExcelVo = new CategoryExcelVo();
                //把category值获取出来，设置到categoryExcelVo里面
                BeanUtils.copyProperties(category, categoryExcelVo);
                categoryExcelVoList.add(categoryExcelVo);
            }
            //3 调用easyexcel的write方法完成写操作
            EasyExcel.write(response.getOutputStream(), CategoryExcelVo.class)
                    .sheet("分类数据").doWrite(categoryExcelVoList);
        } catch (Exception e) {
            e.printStackTrace();
            throw new yxzxException(ResultCodeEnum.DATA_ERROR);
        }
    }

    @Override
    public void importData(MultipartFile file) {
        //监听器
        ExcelListener<CategoryExcelVo> excelListener = new ExcelListener(categoryMapper);
        try {
            EasyExcel.read(file.getInputStream(), CategoryExcelVo.class, excelListener)
                    .sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
            throw new yxzxException(ResultCodeEnum.DATA_ERROR);
        }
    }


}
