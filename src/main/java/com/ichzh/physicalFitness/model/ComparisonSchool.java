package com.ichzh.physicalFitness.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.ichzh.physicalFitness.model.AdmissionCondition.AdmissionConditionBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "comparison_school")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComparisonSchool {

    @Id
    @GeneratedValue
    @Column(name="comparison_id")
    private Integer comparisonId;
    
    // 会员ID
    @Column(name="member_id")
    private String memberId;
    
    // 学校标识号
    @Column(name="school_id")
    private Integer schoolId;
    
    // 学校类型 1: 中小学 2: 幼儿园
    @Column(name="school_type")
    private Integer schoolType;
    
    // 序号
    @Column(name="order_num")
    private Integer orderNum;
    
    // 学校名称
    @Transient
    private String schoolName;
    
    // 学校发展
    @Transient
    private Float schoolDevelopment; 
    
    // 生源情况
    @Transient
    private Float studentSource;     
    
    // 师资力量
    @Transient
    private Float facultyStrength;  
    
    // 名师资源
    @Transient
    private Float famousTeacherRes; 
    
    // 硬件条件
    @Transient
    private Float hardwareCondition; 
    
    // 热力(显示值)
    @Transient
    private Float heatingValue4Show;
    
    
    //校园规模——幼儿园
    @Transient
    private Float campusScale;    
    
    //保教工作——幼儿园
    @Transient
    private Float campusDevelopment;     
    
    // 学校标识号
    @Transient
    private Integer choiceId;
    
    //学校代码
    @Transient
    private String schoolCode;
    
    //小学、中学、幼儿园
    @Transient
    private Integer serviceBlock;


}
