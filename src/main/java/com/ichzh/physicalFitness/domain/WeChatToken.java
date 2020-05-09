package com.ichzh.physicalFitness.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeChatToken implements Serializable {

    // 用户唯一标识
    private String openid;

    // 会话密钥
    private String session_key;

    // 用户在开放平台的唯一标识符，在满足 UnionID 下发条件的情况下会返回
    private String unionid;

    // 错误码
    // -1：系统繁忙，此时请开发者稍候再试
    // 0：请求成功
    // 40029：code 无效
    // 45011：频率限制，每个用户每分钟100次
    private String errcode;

    // 错误信息
    private String errmsg;

    // 微信小程序客户端缓存的登录标识
    private String loginKey;
}
