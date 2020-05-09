package com.ichzh.physicalFitness.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
@ConfigurationProperties(prefix = "loginConfig")
@Data
public class LoginConfig {

    // 登录错误次数达到上线后，账号锁定的时间，单位：分钟
    private int lockTime;

    // 允许连续登录失败的错误次数
    private int loginFailNum;

    // 是否开启验证码功能
    private boolean captcha;

    // 允许跨域的域名
    private Set<String> allowDomains;

    // 跨域允许的请求类型
    private String allowMethods;

    // sql语句配置文件
    private String[] sqlYmls;

    // 登录 token 过期时间（分钟）
    private long expirationMinutes;
}
