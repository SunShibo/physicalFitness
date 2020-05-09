package com.ichzh.physicalFitness.dto;

import java.util.Date;

import lombok.Data;

/**
 * 小程序客服端：我的 栏目下的信息
 * @author yjf
 *
 */
@Data
public class MyProfileDto {

	 // 学豆数量
	 private Integer beans;
	 //会员有效期—开始时间
	 private Date beginDate;
	 //会员有效期—截止时间
	 private Date endDate;
	 //会有有效期剩下的天数
	 private Float restDays;
	 //会员绑定的手机号
	 private String mindPhone;
}
