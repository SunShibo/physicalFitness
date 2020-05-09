package com.ichzh.physicalFitness.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "school_map_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolMapInfo {

    @Id
    @GeneratedValue
    @Column(name="map_info_id")
    private Integer mapInfoId;
    
    // 学校所属区
    @Column(name="town")
    private Integer town;
    
    // 学校代码
    @Column(name="school_code")
    private String schoolCode;
    
    // 学校名称
    @Column(name="school_name")
    private String schoolName;
    
    // 校区名称
    @Column(name="campus_name")
    private String campusName;
    
    // 学段
    @Column(name="searning_section")
    private Integer searningSection;
    
    // 统计代码
    @Column(name="statistic_school_code")
    private String statisticSchoolCode;
    
    // 统计学校名称
    @Column(name="statistic_school_name")
    private String statisticSchoolName;
}
