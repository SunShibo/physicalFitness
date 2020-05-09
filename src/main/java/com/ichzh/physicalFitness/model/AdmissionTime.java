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
 * 入学时间
 */
@Entity(name = "admission_time")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdmissionTime implements Serializable {

    // 入学方式标识号
    @Id
    @GeneratedValue
    @Column(name="time_id")
    private Integer timeId;

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

    // 对入学时间的简要描述(标题)
    @Column(name="time_desc")
    private String timeDesc;

    // 入学时间详情
    @Column(name="time_detail_link")
    private String timeDetailLink;
    
    //日程安排时间
    @Column(name="arrange_time")
    private String arrangeTime;
    
    // 所属学年
    @Column(name="year_year")
    private Integer yearYear;
    
    //入学时间分组
    @Column(name="time_group")
    private String timeGroup;
}
