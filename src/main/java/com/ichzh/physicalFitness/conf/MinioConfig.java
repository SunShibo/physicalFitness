package com.ichzh.physicalFitness.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "minioConfig")
@Data
public class MinioConfig {

    // Minio文件上传下载类型配置
    // 文件类型与contentType关系配置
    private Map<String, String> contentTypeMap;

    // 对象存储服务的URL
    private String endpoint;

    // Access key就像用户ID，可以唯一标识你的账户。
    private String accessKey;

    // Secret key是你账户的密码。
    private String secretKey;

    // 文件云存储的bucketName
    private String bucketName;
}
