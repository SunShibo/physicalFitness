package com.ichzh.physicalFitness.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "enrollment_schedule_list")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnrollmentScheduleList {

    @Id
    @GeneratedValue
    @Column(name="schedule_id")
    private Integer scheduleId;
    
    // 服务模块
    @Column(name="service_block")
    private Integer serviceBlock;
    
    //区
    @Column(name="town")
    private Integer town;
    
    //开始日期
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Column(name="schedule_date_begin")
    private Date scheduleDateBegin;
    
    //结束日期
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Column(name="schedule_date_end")
    private Date scheduleDateEnd;
    
    //入学方式
    @Column(name="admission_mode")
    private Integer admissionMode;
    
    //家长做的什么
    @Column(name="parent_do_something")
    private String parentDoSomething;
    
    //管理方做什么
    @Column(name="manager_do_something")
    private String managerDoSomething;
    
    //年度
    @Column(name="year_year")
    private Integer yearYear;
    
    //当天是否改时间段内
    @Transient
    private int isToday;
}
