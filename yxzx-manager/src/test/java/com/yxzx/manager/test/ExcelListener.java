package com.yxzx.manager.test;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * @author: Peter
 * @description:
 * @date: 2024/2/14 21:15
 */
public class ExcelListener<T> extends AnalysisEventListener<T> {
    private List<T> data = new ArrayList<>();

    //读取excel内容，从第二行开始读取，把每行读取内容封装到t对象里面
    @Override
    public void invoke(T t, AnalysisContext analysisContext) {
        data.add(t);
    }

    public List<T> getData() {
        return data;
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
