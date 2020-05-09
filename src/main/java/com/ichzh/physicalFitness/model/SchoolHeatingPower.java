package com.ichzh.physicalFitness.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学校热力信息
 * @author yjf
 *
 */
@Entity(name = "school_heating_power")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolHeatingPower {
	
    @Id
    @GeneratedValue
    @Column(name="heating_power_id")
    private Integer heatingPowerId;
    
    // 学校所属区
    @Column(name="town")
    private Integer town;
    
    // 学校名称
    @Column(name="school_name")
    private String schoolName;
    
    // 学校代码
    @Column(name="school_code")
    private String schoolCode;
    
    // 热力值
    @Column(name="heating_value")
    private Float heatingValue;
    
    // 学校类型 1: 小学  2：初中
    @Column(name="school_type")
    private Integer schoolType;
    
    @Column(name="year_year")
    private Integer yearYear;
    
    // 热力等级
    @Column(name="level")
    private Integer level;
    
    // 学校是否被收藏
    @Transient
    private Integer colStatus;
    
    // 学校对应的学校优选表中的主键
    @Transient
    private Integer schoolId;
    
    // 经度
    @Transient
    private BigDecimal longitude;

    // 维度
    @Transient
    private BigDecimal dimension;
    
}
