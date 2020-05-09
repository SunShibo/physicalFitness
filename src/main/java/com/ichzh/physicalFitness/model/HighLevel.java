package com.ichzh.physicalFitness.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "high_level")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HighLevel {

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
}
