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

@Entity(name = "schedule_reminder")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleReminder {

    @Id
    @GeneratedValue
    @Column(name="reminder_id")
    private Integer reminderId;
    
    // 会员标识号
    @Column(name="member_id")
    private String memberId;
    
    // 服务模块
    @Column(name="service_block")
    private Integer serviceBlock;
    
    //区
    @Column(name="town")
    private Integer town;
    
    // 入学方式
    @Column(name="admission_mode")
    private Integer admissionMode;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name="create_time")
    private Date create_time;
    
    // 服务模块显示名称
    @Transient
    private String serviceBlockName;
    
    // 区县名称
    @Transient
    private String townName;
    
    // 入学方式显示名称
    @Transient
    private String admissionModeName;
    
    
    @Transient
    private String openId;
    
    @Transient
    private String unionId;
    
    @Transient
    private Date scheduleDate;
    
    @Transient
    private String parentDoSomething;
}
