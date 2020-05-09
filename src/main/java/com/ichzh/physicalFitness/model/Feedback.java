package com.ichzh.physicalFitness.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback {

    @Id
    @GeneratedValue
    @Column(name="back_id")
    private Integer backId;
    
    // 会员标识号.
    @Column(name="member_id")
    private String memberId;
    
    // 反馈内容
    @Column(name="content")
    private String content;
    
    // 反馈时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name="back_time")
    private Date backTime;
}
