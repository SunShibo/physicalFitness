package com.ichzh.physicalFitness.domain;

import lombok.Data;

@Data
public class AppletCard {

    // 小卡片标题
    private String title;

    // 小程序appid
    private String appid;

    // 小程序页面
    private String pagepath;

    // 素材id
    private String thumbMediaId;
}
