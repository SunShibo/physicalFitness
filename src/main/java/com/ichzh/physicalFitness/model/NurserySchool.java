package com.ichzh.physicalFitness.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 幼儿园
 */
@Entity(name = "nursery_school")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NurserySchool implements Serializable {

    // 幼儿园标识号
    @Id
    @GeneratedValue
    @Column(name="nursery_school_id")
    private Integer nurserySchoolId;

    // 学校代码
    @Column(name="school_code")
    private String schoolCode;

    // 学校名称
    @Column(name="school_name")
    private String schoolName;

    // 是否公办 0:否 1:是
    @Column(name="is_public")
    private Integer isPublic;

    // 是否民办 0:否 1:是
    @Column(name="is_private")
    private Integer isPrivate;

    // 是否示范 0:否 1:是
    @Column(name="is_model")
    private Integer isModel;

    // 是否民办普惠 0:否 1:是
    @Column(name="is_inclusive")
    private Integer isInclusive;

    // 经度
    @Column(name="longitude")
    private BigDecimal longitude;

    // 维度
    @Column(name="dimension")
    private BigDecimal dimension;
    
    //举办者
    @Column(name="organizers")
    private Integer organizers;
    
    //所属区
    @Column(name="town")
    private Integer town;
    
    //网址
    @Column(name="website_url")
    private String websiteUrl;
    
    //地址
    @Column(name="address")
    private String address;    
    
    //校园规模
    @Column(name="campus_scale")
    private Float campusScale;    
    
    //保教工作
    @Column(name="campus_development")
    private Float campusDevelopment;     
    
    //师资力量
    @Column(name="faculty_strength")
    private Float facultyStrength; 
    
    //硬件条件
    @Column(name="hardware_condition")
    private Float hardwareCondition; 
    
    //学校简介
    @Column(name="school_profile")
    private String schoolProfile;     
    
    //学校历史
    @Column(name="school_history")
    private String schoolHistory;
    
    //将学校历史拆分为多个段落
    @Transient
    private List<String> listSchoolHistory;
    
    //学校位置
    @Column(name="school_address")
    private String schoolAddress;    
    
    //将学校位置信息拆分为多个段落
    @Transient
    private List<String> listSchoolAddress;
    
    //办学理念
    @Column(name="running_idea")
    private String runningIdea;    
    
    //将办学理念拆分为多个段落
    @Transient
    private List<String> listRunningIdea;
    
    //学校规模
    @Column(name="school_size")
    private String schoolSize; 
    
    //将学校规模拆分为多个段落
    @Transient
    private List<String> listSchoolSize;
    
    //学校特色
    @Column(name="school_character")
    private String schoolCharacter;      
    
    // 将学校特色拆分为多个段落
    @Transient
    private List<String> listSchoolCharacter;
    
 // 所属学年
    @Column(name="year_year")
    private Integer yearYear;
    
    @Transient
    private Integer schoolId;
    //收藏状态
    @Transient
    private Integer colStatus;
    
    //学校标签
    @Transient
    private List<SchoolLabel> schoolLabeles;
    
    //举办者名称
    @Transient
    private String organizersName;
    //举办者描述
    @Transient
    private String organizersDesc;
}
