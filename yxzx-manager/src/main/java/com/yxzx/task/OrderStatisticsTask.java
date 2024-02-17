package com.yxzx.task;

import cn.hutool.core.date.DateUtil;
import com.yxzx.mapper.OrderInfoMapper;
import com.yxzx.mapper.OrderStatisticsMapper;
import com.yxzx.model.entity.order.OrderStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/16 18:19
 */

@Component
public class OrderStatisticsTask {
    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private OrderStatisticsMapper orderStatisticsMapper;

    //测试定时任务:每隔5秒执行一次
    //注解Scheduled +cron表达式
    /*@Scheduled(cron = "0/5 * * * * ? ")
    public void testHello() {
        System.out.println(new Date().toInstant());
    }*/

    //每天凌晨2点，查询前一天日期统计数据，把统计之后数据添加统计结果表里面
    @Scheduled(cron = "0 0 2 * * ?")
    //@Scheduled(cron = "0/5 * * * * ? ") //测试
    public void orderTotalAmountStatistics() {
        System.out.println(new Date().toInstant());
        //1 获取前一天日期
        String createDate = DateUtil.offsetDay(new Date(), -1).toString("yyyy-MM-dd");

        //2 根据前一天日期进行统计功能，（分组操作）
        //统计前一天交易金额
        OrderStatistics orderStatistics = orderInfoMapper.selectStatisticsByDate(createDate);

        //3 把统计之后的数据，添加统计结果到表里面
        if(orderStatistics != null){
            orderStatisticsMapper.insert(orderStatistics);
        }

    }


}
