package com.ichzh.physicalFitness.domain;

import lombok.Data;

@Data
public class WeChatAuthentication {

    private String signature;

    private String timestamp;

    private String nonce;

    private String echostr;
}
