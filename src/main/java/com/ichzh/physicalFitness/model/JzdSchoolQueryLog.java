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

@Entity(name = "jzd_school_query_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JzdSchoolQueryLog {

    @Id
    @GeneratedValue
    @Column(name="log_id")
    private Integer logId;
    
    //会员标识号
    @Column(name="member_id")
    private String memberId;
    
    // 查询的小区
    @Column(name="detail_address")
    private String detailAddress;
    
    
    //查询的时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name="query_time")
    private Date queryTime;
    
    // 入学阶段
    @Column(name="service_block")
    private Integer serviceBlock;
    
    //入学区域
    @Column(name="town")
    private Integer town;
}
