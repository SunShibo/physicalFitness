package com.ichzh.physicalFitness.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "member_share")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberShare {
	
	// 居住地对应学校标识号
    @Id
    @GeneratedValue
    @Column(name="share_id")
    private Integer shareId;

    @Column(name="member_id")
    private String memberId;
    
    @Column(name="open_id")
    private String openId;
    
    @Column(name="share_time")
    private Date shareTime;
     
    @Column(name="is_calculated")
    private Integer isCalculated;
    
    @Column(name="calculated_time")
    private Date calculatedTime;
    
    @Column(name="is_calculated_pay")
    private Integer isCalculatedPay;
    
    @Column(name="calcaulated_time_pay")
    private Date calcaulatedTimePay;
    
}
