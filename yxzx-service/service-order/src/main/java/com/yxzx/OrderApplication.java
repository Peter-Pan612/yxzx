package com.yxzx;

import com.yxzx.annotation.EnableUserLoginAuthInterceptor;
import com.yxzx.annotation.EnableUserTokenFeignInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/17 17:44
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.yxzx"})
@EnableUserTokenFeignInterceptor
@EnableUserLoginAuthInterceptor
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }
}
