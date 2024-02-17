package com.yxzx.service;

import com.github.pagehelper.PageInfo;
import com.yxzx.model.dto.h5.OrderInfoDto;
import com.yxzx.model.entity.order.OrderInfo;
import com.yxzx.model.vo.h5.TradeVo;

public interface OrderInfoService {
    TradeVo getTrade();

    Long submitOrder(OrderInfoDto orderInfoDto);

    OrderInfo getOrderInfo(Long orderId);

    TradeVo buy(Long skuId);

    PageInfo<OrderInfo> findUserPage(Integer page, Integer limit, Integer orderStatus);
}
