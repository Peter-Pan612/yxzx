package com.yxzx.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: Peter
 * @description:
 * @date: 2024/2/7 16:09
 */

@Data
@ConfigurationProperties(prefix = "yxzx.minio")
public class MinioProperties {

    private String endpointUrl;
    private String accessKey;
    private String secreKey;
    private String bucketName;

}
