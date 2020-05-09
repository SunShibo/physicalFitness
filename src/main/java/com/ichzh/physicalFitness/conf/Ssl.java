package com.ichzh.physicalFitness.conf;

import lombok.Data;

@Data
public class Ssl {

    // 证书编译路径
    private String keyStore;

    // 证书类型
    private String keyStoreType;

    // https 运行中间件
    private String keyAlias;

    // 生成证书时的密码
    private String keyStorePassword;
}
