package com.ichzh.physicalFitness.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学校标签
 * @author yjf
 *
 */
@Entity(name = "school_label")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolLabel {

    @Id
    @GeneratedValue
    @Column(name="school_label_id")
    private Integer schoolLabelId;
    
    // 标签名称
    @Column(name="label_name")
    private String label_name;
    
    // 标签描述
    @Column(name="label_desc")
    private String labelDesc;
    
    //学校代码
    @Column(name="school_code")
    private String schoolCode;
}
