package com.yxzx;

import com.yxzx.commom.log.annotation.EnableLogAspect;
import com.yxzx.properties.MinioProperties;
import com.yxzx.properties.UserProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author: Peter
 * @description:
 * @date: 2024/1/27 20:43
 */

@SpringBootApplication
@ComponentScan(basePackages = {"com.yxzx.*"})
@EnableConfigurationProperties(value = {UserProperties.class, MinioProperties.class})
@EnableScheduling
@EnableLogAspect
public class ManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ManagerApplication.class, args);
    }
}
