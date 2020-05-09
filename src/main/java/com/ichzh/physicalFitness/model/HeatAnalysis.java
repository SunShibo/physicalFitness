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

@Entity(name = "heat_analysis")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HeatAnalysis {

    @Id
    @GeneratedValue
    @Column(name="analysis_id")
    private Integer analysisId;
    
    //服务模块 
    @Column(name="service_block")
    private Integer serviceBlock;
    
    //区
    @Column(name="town")
    private Integer town;
    
    //学校代码
    @Column(name="school_code")
    private String schoolCode;
    
    //学校名称
    @Column(name="school_name")
    private String schoolName;
    
    //入学方式
    @Column(name="admission_mode")
    private Integer admissionMode;
    
    //占比
    @Column(name="ratio")
    private BigDecimal ratio;
    
    //年度
    @Column(name="year_year")
    private Integer yearYear;
    
    //占比等级(1 小  2 中  3 大)
    @Transient
    private Integer radioLevel;
    
    //入学方式名称
    @Transient
    private String admissionModeName;
}
