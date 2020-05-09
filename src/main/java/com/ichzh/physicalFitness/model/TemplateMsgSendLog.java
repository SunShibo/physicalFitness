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

@Entity(name = "template_msg_send_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateMsgSendLog {

    @Id
    @GeneratedValue
    @Column(name="log_id")
    private Integer logId;
    
    // 公众号openId
    @Column(name="gzh_open_id")
    private String gzhOpenId;
    
    // 发送内容
    @Column(name="send_content")
    private String sendContent;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name="send_time")
    private Date sendTime;
}
