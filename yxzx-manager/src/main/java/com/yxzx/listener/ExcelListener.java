package com.yxzx.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.yxzx.mapper.CategoryMapper;
import com.yxzx.model.vo.product.CategoryExcelVo;

import java.util.List;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/15 19:29
 */
public class ExcelListener<T> implements ReadListener<T> {

    /**
     * 每隔5条存储数据库，实际使用中可以100条，然后清理list ，方便内存回收
     */
    private static final int BATCH_COUNT = 100;
    /**
     * 缓存的数据
     */
    private List<T> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    //通过构造传递mapper
    private CategoryMapper categoryMapper;

    public ExcelListener(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    //从第二行开始读取，把每行读取内容封装到t对象里面
    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        //把每行数据对象t放到cachedDateList里面
        cachedDataList.add(t);
        //达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if ((cachedDataList.size() >= BATCH_COUNT)) {
            //调用方法一次性批量添加数据库里面
            saveData();
            //清理list集合
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        //保存数据
        saveData();
    }

    //保存方法
    private void saveData() {
        categoryMapper.batcheInsert((List<CategoryExcelVo>) cachedDataList);
    }

}
