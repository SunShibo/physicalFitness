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

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 自主招生
 */
@Entity(name = "independent_recruitment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndependentRecruitment implements Serializable {

    // 入学方式标识号
    @Id
    @GeneratedValue
    @Column(name="recruiment_id")
    private Integer recruimentId;

    // 入学方式所属服务模块
    // 10001 上幼儿园
    //10002 上小学
    //10003 上初中
    //10004 中考中招
    //10005 高考高招
    @Column(name="service_block")
    private Integer serviceBlock;

    // 所属区
    @Column(name="town")
    private Integer town;

    // 学校代码
    @Column(name="school_code")
    private String schoolCode;

    // 学校名称
    @Column(name="school_name")
    private String schoolName;

    // 是否全市招生
    //0: 否 1：是
    @Column(name="is_city")
    private Integer isCity;
    
    //所属学年
    private Integer yearYear;
    
    //自主招生入学方式
    @Column(name="admission_mode")
    private Integer admissionMode;
    
    @Transient
    private BigDecimal longitude;

    // 维度
    @Transient
    private BigDecimal dimension;
    
    @Transient
    private Integer choiceId;
    
    @Transient
    private Integer schoolId;
    
    //收藏状态
    @Transient
    private Integer colStatus;
}
