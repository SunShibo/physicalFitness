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
 * 小学出口
 */
@Entity(name = "prim_school_export")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrimSchoolExport {

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
    
    // 小学学校代码
    @Column(name="prim_school_code")
    private String primSchoolCode;
    
    // 升学占比
    @Column(name="sx_ratio")
    private Float sxRatio;
    
    // 初中学校代码
    @Column(name="middle_school_code")
    private String middleSchoolCode;
    
    // 初中校学校名称
    @Column(name="middle_school_name")
    private String middleSchoolName;
    
    // 初中校校区名称
    @Column(name="middle_campus_name")
    private String middleCampusName;
    
    
}
