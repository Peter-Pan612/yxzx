package com.yxzx.service;

import com.yxzx.model.dto.order.OrderStatisticsDto;
import com.yxzx.model.vo.order.OrderStatisticsVo;

public interface OrderInfoService {
    OrderStatisticsVo getOrderStatisticsData(OrderStatisticsDto orderStatisticsDto);
}
