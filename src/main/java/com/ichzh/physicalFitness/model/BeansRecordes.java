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
import java.util.Date;

/**
 * 学豆获得或消费记录
 */
@Entity(name = "beans_recordes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeansRecordes implements Serializable {

    // 记录标识号
    @Id
    @GeneratedValue
    @Column(name="record_id")
    private Integer recordId;

    // 会员ID
    @Column(name="member_id")
    private String memberId;

    // 获得或消费
    // 1、 获得学豆 2、消费学豆
    @Column(name="record_type")
    private Integer recordType;

    // 记录时间
    @Column(name="record_time")
    private Date recordTime;

    // 学豆数量
    @Column(name="beans_num")
    private Integer beansNum;
}
