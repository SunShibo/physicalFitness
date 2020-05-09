package com.ichzh.physicalFitness.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 初中出口 
 * @author yjf
 *
 */
@Entity(name = "middle_school_export")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MiddleSchoolExport {

    @Id
    @GeneratedValue
    @Column(name="export_id")
    private Integer exportId;
    
    // 年度
    @Column(name="year")
    private Integer year;
    
    // 所在区
    @Column(name="town")
    private Integer town;
    
    
    // 初中学校代码
    @Column(name="middle_school_code")
    private String middleSchoolCode;
    
    // 升学占比
    @Column(name="sx_ratio")
    private Float sxRatio;
    
    // 高中学校代码
    @Column(name="high_school_code")
    private String highSchoolCode;
    
    // 高中校学校名称
    @Column(name="high_school_name")
    private String highSchoolName;
    
    // 高中校校区名称
    @Column(name="high_campus_name")
    private String highCampusName;
}
