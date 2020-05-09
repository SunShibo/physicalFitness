package com.ichzh.physicalFitness.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 入学政策
 */
@Entity(name = "admission_policy")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdmissionPolicy implements Serializable {

    // 入学政策标识号
    @Id
    @GeneratedValue
    @Column(name="policy_id")
    private Integer policyId;

    // 入学政策所属服务模块
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

    // 政策摘要
    @Column(name="policy_summary")
    private String policySummary;

    // 相关链接
    @Column(name="link")
    private String link;
    
    // 政策名称
    @Column(name="policy_name")
    private String policyName;    
    
    // 所属学年
    @Column(name="year_year")
    private Integer yearYear;
}
