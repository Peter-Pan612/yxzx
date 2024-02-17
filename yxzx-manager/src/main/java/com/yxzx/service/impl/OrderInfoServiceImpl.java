package com.yxzx.service.impl;

import cn.hutool.core.date.DateUtil;
import com.yxzx.mapper.OrderStatisticsMapper;
import com.yxzx.model.dto.order.OrderStatisticsDto;
import com.yxzx.model.entity.order.OrderStatistics;
import com.yxzx.model.vo.order.OrderStatisticsVo;
import com.yxzx.service.OrderInfoService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/16 19:46
 */
@Service
public class OrderInfoServiceImpl implements OrderInfoService {
    @Autowired
    private OrderStatisticsMapper orderStatisticsMapper;

    @Override
    public OrderStatisticsVo getOrderStatisticsData(OrderStatisticsDto orderStatisticsDto) {
        //根据dto条件查询统计结果数据，返回List集合
        List<OrderStatistics> orderStatisticsList = orderStatisticsMapper.selectList(orderStatisticsDto);

        //遍历List集合，得到所有日期，把所有日期封装list新集合里面

        List<String> dateList = orderStatisticsList.stream()
                .map(orderStatistics -> DateUtil.format(orderStatistics.getOrderDate(), "yyyy-MM-dd"))
                .collect(Collectors.toList());

        //遍历list集合，得到所有日期对应总金额，把总金额封装list新集合里面
        List<BigDecimal> decimalList = orderStatisticsList.stream().map(OrderStatistics::getTotalAmount).collect(Collectors.toList());


        //把两个list集合封装OrderStatisticsVo，返回OrderStatisticsVo
        OrderStatisticsVo orderStatisticsVo = new OrderStatisticsVo();
        orderStatisticsVo.setDateList(dateList);
        orderStatisticsVo.setAmountList(decimalList);


        return orderStatisticsVo;
    }
}
