package com.yxzx.mapper;

import com.yxzx.model.entity.order.OrderStatistics;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderInfoMapper {
    OrderStatistics selectStatisticsByDate(String createDate);
}
