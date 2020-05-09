package com.ichzh.physicalFitness.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "member_commodity_num")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberCommodityNum {

    @Id
    @GeneratedValue
    @Column(name="mc_num_id")
    private Integer mcNumId;
    
    //api编号
    @Column(name="api_code")
    private Integer apiCode;
    
    //会员标识号
    @Column(name="member_id")
    private String memberId;
    
    //免费使用次数
    @Column(name="free_num")
    private Integer freeNum;
    
    //购买或用学豆换的次数
    @Column(name="buy_exchange_num")
    private Integer buyExchangeNum;
    
}
