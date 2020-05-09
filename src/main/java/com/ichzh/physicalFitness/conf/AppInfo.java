package com.ichzh.physicalFitness.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 项目基本信息
 */
@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class AppInfo {

    private String name;

    private String description;

    private String version;
}
