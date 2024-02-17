package com.yxzx.feign.cart;

import com.yxzx.model.entity.h5.CartInfo;
import com.yxzx.model.vo.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "service-cart")
public interface CartFeignClient {

    @GetMapping(value = "/api/order/cart/auth/getAllCkecked")
    public abstract List<CartInfo> getAllCkecked();

    @GetMapping("/api/order/cart/auth/deleteChecked")
    public Result deleteChecked();
}
