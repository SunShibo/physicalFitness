package com.ichzh.physicalFitness.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ichzh.physicalFitness.model.IndependentRecruitmentQueryRecord.IndependentRecruitmentQueryRecordBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "independent_recruitment_query_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IndependentRecruitmentQueryLog {

    @Id
    @GeneratedValue
    @Column(name="log_id")
    private Integer logId;
    
    // 会员标识号
    @Column(name="member_id")
    private String memberId;
    
    // 查询的区
    @Column(name="town")
    private Integer town;
    
    // 查询的区名称
    @Column(name="town_name")
    private String townName;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name="query_time")
    private Date queryTime;
}
