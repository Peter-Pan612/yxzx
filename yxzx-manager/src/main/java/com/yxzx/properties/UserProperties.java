package com.yxzx.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/5 19:50
 */

@Data
@ConfigurationProperties(prefix = "yxzx.auth")
public class UserProperties {

    private List<String> noAuthUrls;
}
