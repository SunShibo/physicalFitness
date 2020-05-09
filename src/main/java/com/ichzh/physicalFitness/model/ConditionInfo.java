package com.ichzh.physicalFitness.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 筛选条件记录
 * @author yjf
 *
 */
@Entity(name = "condition_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConditionInfo implements Serializable{

    // 入学方式标识号
    @Id
    @GeneratedValue
    @Column(name="cond_info_id")
    private Integer condInfoId;
    
    // 会员ID
    @Column(name="member_id")
    private String memberId;
    
    // 服务模块
    @Column(name="service_block")
    private Integer serviceBlock;
    
    // 入学区域
    @Column(name="town")
    private Integer town;
    
    // 学籍所在地
    @Column(name="xj_address")
    private Integer xjAddress;
    
    // 户籍所在地
    @Column(name="hj_address")
    private Integer hjAddress;
    
    // 居住地
    @Column(name="jzd_address")
    private Integer jzdAddress;
    
}
