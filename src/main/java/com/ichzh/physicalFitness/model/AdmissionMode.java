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
 * 入学方式
 */
@Entity(name = "admission_mode")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdmissionMode implements Serializable {

    // 入学方式标识号
    @Id
    @GeneratedValue
    @Column(name="mode_id")
    private Integer modeId;

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

    // 方式所属分类 字典
    @Column(name="mode_kind")
    private Integer modeKind;

    // 方式描述 对入学方式的简单描述
    @Column(name="mode_desc")
    private String modeDesc;

    // 方式详情
    //外链到 微信公众号的 一篇文章
    @Column(name="mode_detail_link")
    private String modeDetailLink;
    
    // 所属学年
    @Column(name="year_year")
    private Integer yearYear;
}
