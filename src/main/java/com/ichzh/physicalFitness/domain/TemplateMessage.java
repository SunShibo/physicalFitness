package com.ichzh.physicalFitness.domain;

import lombok.Data;

import java.util.Map;

/**
 * 客服推送模板消息参数对象
 * 注：url和miniprogram都是非必填字段，若都不传则模板无跳转；若都传，会优先跳转至小程序。开发者可根据实际需要选择其中一种跳转方式即可。当用户的微信客户端版本不支持跳小程序时，将会跳转至url。
 *
 */
@Data
public class TemplateMessage {

    // 接收者openid
    private String touser;

    // 模板ID
    private String template_id;

    // 模板跳转链接（海外帐号没有跳转能力）
    private String url;

    // 跳小程序所需数据，不需跳小程序可不用传该数据
    private MiniProgram miniProgram;

    // 模板数据
    private Map<String, Map<String, String>> data;

    // 模板内容字体颜色，不填默认为黑色
    private String color;
}
