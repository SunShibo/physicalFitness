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

@Entity(name = "recruit_range_query_record")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecruitRangeQueryRecord {

    @Id
    @GeneratedValue
    @Column(name="record_id")
    private Integer recordId;
    
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
    
    // 街道名称
    @Column(name="street_name")
    private String streetName;
    
    // 社区名称
    @Column(name="community_name")
    private String communityName;
    
    // 住址名称
    @Column(name="detail_address")
    private String detailAddress;
    
    // 入学阶段
    @Column(name="service_block")
    private Integer serviceBlock;
    
    //入学区域
    @Column(name="town")
    private Integer town;
    
    //区名称
    @Transient
    private String townName;
}
