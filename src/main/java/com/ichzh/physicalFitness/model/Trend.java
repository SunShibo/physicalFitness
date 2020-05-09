package com.ichzh.physicalFitness.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "trend")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trend {

    @Id
    @GeneratedValue
    @Column(name="trend_id")
    private Integer trendId;
    
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
    
    //志愿填报人数
    @Column(name="volunteers_num")
    private Integer volunteersNum;
    
    //录取人数
    @Column(name="enrollment_num")
    private BigDecimal enrollmentNum;
    
    //年度
    @Column(name="year_year")
    private Integer yearYear;
}
