package com.ichzh.physicalFitness.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "middle_level")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MiddleLevel {

    @Id
    @GeneratedValue
    @Column(name="level_id")
    private Integer levelId;
    
    // 学校代码
    @Column(name="school_code")
    private String schoolCode;
    
    // 学校等级
    @Column(name="level")
    private Integer level;
    
    // 是否寄宿
    @Column(name="is_board")
    private Integer isBoard;
    
    // 是否民办
    @Column(name="is_private")
    private Integer isPrivate;
    
    // 是否特色
    @Column(name="is_feature")
    private Integer isFeature;
    
    // 是否改革
    @Column(name="is_reform")
    private Integer isReform;
    
}
