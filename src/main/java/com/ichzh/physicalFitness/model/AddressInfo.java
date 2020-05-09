package com.ichzh.physicalFitness.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会员地址信息
 * @author yjf
 *
 */
@Entity(name = "address_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressInfo {

    @Id
    @GeneratedValue
    @Column(name="address_id")
    private Integer addressId;
    
    // 会员ID
    @Column(name="member_id")
    private String memberId;
    
    // 详细地址
    @Column(name="address_detail")
    private String addressDetail;
    
    // 经度
    @Column(name="longitude")
    private BigDecimal longitude;

    // 维度
    @Column(name="dimension")
    private BigDecimal dimension;
    
    // 地址类型 1：家庭地址 2：单位地址
    @Column(name="address_type")
    private Integer addressType;
    
    // 是否默认地址 (1: 是 0：否)
    @Column(name="if_default")
    private Integer ifDefault;
}
