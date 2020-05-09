package com.ichzh.physicalFitness.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ichzh.physicalFitness.model.ConditionInfo.ConditionInfoBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 学校收藏记录
 * @author yjf
 *
 */
@Entity(name = "school_collection")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolCollection  implements Serializable{

    @Id
    @GeneratedValue
    @Column(name="collection_id")
    private Integer collectionId;
    
    // 会员ID
    @Column(name="member_id")
    private String memberId;
    
    // 服务模块
    @Column(name="service_block")
    private Integer serviceBlock;
    
    // 收藏学校的标识号
    @Column(name="school_id")
    private Integer schoolId;
    
    //收藏时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name="collection_time")
    private Date collectionTime;
    
    //学校名称
    @Transient
    private String schoolName;
    
    //学校所属区
    @Transient
    private Integer town;
    
    //区名称
    @Transient
    private String townName;
}
