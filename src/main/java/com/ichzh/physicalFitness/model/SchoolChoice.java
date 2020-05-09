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
import java.util.Map;

/**
 * 学校优选
 */
@Entity(name = "school_choice")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolChoice implements Serializable {

    // 入学方式标识号
    @Id
    @GeneratedValue
    @Column(name="choice_id")
    private Integer choiceId;

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

    // 学校代码
    @Column(name="school_code")
    private String schoolCode;

    // 学校名称
    @Column(name="school_name")
    private String schoolName;
    
    // 办学类型
    @Column(name="school_running_type")
    private Integer schoolRunningType;    
    
    // 举办者
    @Column(name="organizers")
    private Integer organizers; 
    
    // 地址
    @Column(name="address")
    private String address;  
    
    // 网址
    @Column(name="website")
    private String website; 
    
    // 学校发展
    @Column(name="school_development")
    private Float schoolDevelopment; 
    
    // 学校发展评价描述
    @Transient
    private String schoolDevelopmentDesc;
    
    // 生源情况
    @Column(name="student_source")
    private Float studentSource;     
    
    // 生源情况评价描述
    @Transient
    private String studentSourceDesc;
    
    // 师资力量
    @Column(name="faculty_strength")
    private Float facultyStrength;  
    
    // 师资力量评价描述
    @Transient
    private String facultyStrengthDesc;
    
    // 名师资源
    @Column(name="famous_teacher_res")
    private Float famousTeacherRes; 
    
    // 名师资源评价描述
    @Transient
    private String famousTeacherResDesc;
    
    // 硬件条件
    @Column(name="hardware_condition")
    private Float hardwareCondition; 
    
    // 硬件条件评价描述
    @Transient
    private String hardwareConditionDesc;
    
    // 所属学年
    @Column(name="year_year")
    private Integer yearYear;
    
    // 经度
    @Column(name="longitude")
    private BigDecimal longitude;

    // 维度
    @Column(name="dimension")
    private BigDecimal dimension;
    
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
    
    // 统计代码
    @Column(name="statistic_school_code")
    private String statisticSchoolCode;
    
    //将办学理念拆分为多个段落
    @Transient
    private List<String> listRunningIdea;
    
    //学校规模
    @Column(name="school_size")
    private String schoolSize; 
    
    //现任校长姓名
    @Column(name="school_master_name")
    private String schoolMasterName;
    
    //校长照片URL
    @Column(name="school_master_url")
    private String schoolMasterUrl;
    
    //历史沿革
    @Column(name="historical_evolution")
    private String historicalEvolution;
    
    //高光时刻
    @Column(name="best_moments")
    private String bestMoments;
    
    //教职工数
    @Column(name="teachers")
    private Integer teachers;
    
    //专任教师
    @Column(name="teachers_zr")
    private Integer teachersZr;
    
    //学科带头人
    @Column(name="teachers_xkdtr")
    private Integer teachersXkdtr;
    
    //高级教师
    @Column(name="teachers_gjjs")
    private Integer teachersGjjs;
    
    //中级教师
    @Column(name="teachers_zjjs")
    private Integer teachersZjjs;
    
    //博士生
    @Column(name="doctor")
    private Integer doctor;
    
    //研究生
    @Column(name="post_graduate")
    private Integer postGraduate;
    
    //本科生
    @Column(name="undergraduate")
    private Integer undergraduate;
    
    //毕业生
    @Column(name="graduate")
    private Integer graduate;
    
    //招生
    @Column(name="recruit")
    private Integer recruit;
    
    //在校生
    @Column(name="students")
    private Integer students;
    
    //班级数
    @Column(name="class_number")
    private Integer classNumber;
    
    //占地面积
    @Column(name="area")
    private BigDecimal area;
    
    //运动场面积
    @Column(name="sports_area")
    private BigDecimal sportsArea;
    
    //将学校规模拆分为多个段落
    @Transient
    private List<String> listSchoolSize;
    
    //学校特色
    @Column(name="school_character")
    private String schoolCharacter;      
    
    // 将学校特色拆分为多个段落
    @Transient
    private List<String> listSchoolCharacter;
    
    // 热力原始值
    @Transient
    private Float heatingValue;
    
    // 热力(显示值 原始值 X 100)
    @Transient
    private Float heatingValue4Show;
    
    //热力等级对应的学校——等级一
    @Transient
    private List<SchoolHeatingPower> levelones;
    
    //热力等级对应的学校——等级二
    @Transient
    private List<SchoolHeatingPower> leveltwos;
    
    //热力等级对应的学校——等级三
    @Transient
    private List<SchoolHeatingPower> levelthrees;
    
    //热力等级对应的学校——等级四
    @Transient
    private List<SchoolHeatingPower> levelfours;
    
    //热力等级对应的学校——等级五
    @Transient
    private List<SchoolHeatingPower> levelfives;
    
    //学校标签
    @Transient
    private List<SchoolLabel> schoolLabeles;
    
    /**
   	 * 学校是否被收藏( 1 收藏  0 未收藏)
   	 */
    @Transient
   	private Integer colStatus;
    
    // 办学类型名称
    @Transient
    private String schoolRunningTypeName;
    
    //办学类型的描述
    @Transient
    private String schoolRuningTypeDesc;
    
    //学校历史是否显示
    @Transient
    private int schoolHistoryIfShow;
    
    //师资情况是否显示
    @Transient
    private int teacherCaseIfShow;
    
    //学生情况是否显示
    @Transient
    private int studentCaseIfShow;
    
    //硬件条件是否显示
    @Transient
    private int hardwareConditionIfShow;
    
    //历史沿革
    @Transient
    private List<Map<String, String>> historyEvolutions;
    
    //热力分析
    @Transient
    private List<HeatAnalysis> HeatAnalysises;
    
    //连年趋势数据
    @Transient
    private List<Trend> trends;
    
    //排名数据(按区)
    @Transient
    private Map<String, Object> ranking;
    
  //排名数据(按片区)
    @Transient
    private Map<String, Object> rankingArea;
    
}
