package com.ichzh.physicalFitness.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "info_latest_year")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfoLatestYear {

    @Id
    @GeneratedValue
    @Column(name="info_year_id")
    private Integer infoYearId;
    
    //服务模块
    @Column(name="service_block")
    private Integer serviceBlock;
    
    //区
    @Column(name="town")
    private Integer town;
    
    //功能编号
    @Column(name="function_code")
    private Integer functionCode;
    
    //功能对应的信息更新到了哪一年
    @Column(name="year_year")
    private Integer yearYear;
    
    
}
