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

@Entity(name = "recruit_range_query_result_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruitRangeQueryResultRecord {

    @Id
    @GeneratedValue
    @Column(name="record_id")
    private Integer recordId;
    
    // 会员标识号
    @Column(name="member_id")
    private String memberId;
    
    // 查询的学校名称
    @Column(name="school_name")
    private String schoolName;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name="query_time")
    private Date queryTime;
    
    // 住址名称
    @Column(name="address")
    private String address;
}
