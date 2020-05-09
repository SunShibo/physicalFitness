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
 * 派位范围
 */
@Entity(name = "aided_allocate_range")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AidedAllocateRange implements Serializable {

    // 派位范围标识号
    @Id
    @GeneratedValue
    @Column(name="range_id")
    private Integer rangeId;

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

    // 派位类型
    @Column(name="aided_allocate_kind")
    private Integer aidedAllocateKind;

    // 学区或片区名称
    @Column(name="school_district_name")
    private String schoolDistrictName;

    // 小学学校代码
    @Column(name="primary_school_code")
    private String primarySchoolCode;

    // 小学学校名称
    @Column(name="primary_school_name")
    private String primarySchoolName;

    // 初中学校代码
    @Column(name="junior_middle_code")
    private String juniorMiddleCode;

    // 初中学校名称
    @Column(name="junior_middle_name")
    private String juniorMiddleName;
    
    // 所属学年
    @Column(name="year_year")
    private Integer yearYear;
    
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
