package com.ichzh.physicalFitness.model;

import java.io.Serializable;
import java.util.List;

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
 * 入学条件
 */
@Entity(name = "admission_condition")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdmissionCondition implements Serializable {

    // 入学方式标识号
    @Id
    @GeneratedValue
    @Column(name="condition_id")
    private Integer conditionId;

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

    // 条件所属分类 字典
    @Column(name="condition_kind")
    private Integer conditionKind;

    // 条件描述 对入学方式的简单描述
    @Column(name="condition_desc")
    private String conditionDesc;

    // 标签_学籍
    @Column(name="student_status")
    private Integer studentStatus;
    
    // 标签_户籍
    @Column(name="household_registration")
    private Integer householdRegistration;  
    
    // 标签_居住
    @Column(name="residence")
    private Integer residence;      
    
    // 条件详情
    @Column(name="condition_detail")
    private String conditionDetail;
    
    // 所属学年
    @Column(name="year_year")
    private Integer yearYear;
    
    // 是否能入学
    @Column(name="is_can_admission")
    private Integer isCanAdmission;
    
    //将入学条件用字符##分割为多个段落
    @Transient
    private List<String> lstConditionDesc;
    
    
    
}
