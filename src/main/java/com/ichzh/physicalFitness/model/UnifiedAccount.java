package com.ichzh.physicalFitness.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * 统一账户信息表
 */
@Entity(name = "unified_account")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnifiedAccount {

    // 统一账户物理id
    @Id
    @GeneratedValue
    @Column(name="unified_account_id")
    private Integer unifiedAccountId;

    // 统一账户开发平台id
    @Column(name="union_id")
    private String unionId;

    // 小程序或公众号的appid
    @Column(name="app_id")
    private String appId;

    // 用户的openId
    @Column(name="open_id")
    private String openId;

    // 类型（1：小程序；2：公众号）
    @Column(name="type")
    private Integer type;
    
    @Transient
    private String gzhOpenId;
    
    @Transient
    private String wechatOpenId;
}
