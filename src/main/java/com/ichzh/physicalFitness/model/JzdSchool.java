package com.ichzh.physicalFitness.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 居住地对应学校
 */
@Entity(name = "jzd_school")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JzdSchool implements Serializable {

	private static final long serialVersionUID = 1L;

	// 居住地对应学校标识号
    @Id
    @GeneratedValue
    @Column(name="jzd2school_id")
    private Integer jzd2school_id;

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

    // 居住地_街道名称
    @Column(name="street_name")
    private String streetName;

    // 居住地_社区名称
    @Column(name="community_name")
    private String communityName;

    // 居住地_住址名称
    @Column(name="detail_address")
    private String detailAddress;

    // 对应的学校代码
    @Column(name="school_code")
    private String schoolCode;

    // 对应的学校名称
    @Column(name="school_name")
    private String schoolName;
    
    // 查询学校名称
    @Column(name="query_school_name")
    private String querySchoolName;
    
    // 所属学年
    @Column(name="year_year")
    private Integer yearYear;
    
    @Transient
    private BigDecimal longitude;

    // 维度
    @Transient
    private BigDecimal dimension;
    
    @Transient
    private Integer choiceId;
    
    @Transient
    private Integer schoolId;
    
    //收藏状态
    @Transient
    private Integer colStatus;
}
