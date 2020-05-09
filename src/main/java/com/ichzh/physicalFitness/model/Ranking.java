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

@Entity(name = "ranking")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ranking {

    @Id
    @GeneratedValue
    @Column(name="ranking_id")
    private Integer rankingId;
    
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
    
    //校区名称
    @Column(name="campus_name")
    private String campusName;
    
    //热度值
    @Column(name="heating_value")
    private BigDecimal heatingValue;
    
    //当前年学校排名
    @Column(name="ranking")
    private Integer ranking;
    
    //年度
    @Column(name="year_year")
    private Integer yearYear;
}
