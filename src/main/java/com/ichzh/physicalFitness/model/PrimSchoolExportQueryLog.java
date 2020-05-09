package com.ichzh.physicalFitness.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "prim_school_export_query_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrimSchoolExportQueryLog {

    @Id
    @GeneratedValue
    @Column(name="log_id")
    private Integer logId;
    
    // 会员标识号
    @Column(name="member_id")
    private String memberId;
    
    // 查询的学校代码
    @Column(name="school_code")
    private String schoolCode;
    
    // 查询的学校名称
    @Column(name="school_name")
    private String schoolName;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name="query_time")
    private Date queryTime;
}
