package com.yxzx;

import com.yxzx.annotation.EnableUserLoginAuthInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/17 11:35
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.yxzx.*"})
@EnableUserLoginAuthInterceptor
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
}
