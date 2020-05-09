package com.ichzh.physicalFitness.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ichzh.physicalFitness.domain.WeChatToken;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 会员
 * @author xqx
 */
@Entity(name = "member")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member implements Serializable {


	// 会员ID
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name="member_id")
	private String memberId;

	// 会员手机
	@Column(name="member_mobile")
	private String memberMobile;

	// 会员登录账号
	@Column(name="member_username")
	private String memberUsername;

	// 会员密登录码
	@Column(name="member_password")
	private String memberPassword;

	// 会员微信
	@Column(name="member_wechat")
	private String memberWeChat;

	// 会员昵称
	@Column(name="member_nickname")
	private String memberNickname;

	// 会员头像
	@Column(name="member_photo")
	private String memberPhoto;

	// 会员性别
	@Column(name="member_sex")
	private Integer memberSex;

	// 会员生日
	@Column(name="member_birthday")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date memberBirthday;

	// 会员积分
	@Column(name="member_integral")
	private int memberIntegral;

	// 会员等级
	//1、 注册会员（绑定了微信号）
	//2、 普通付费会员（绑定了微信号且付费）
	@Column(name="member_grade")
	private Integer memberGrade;

	// 会员拥有的学豆数量
	@Column(name="member_beans")
	private int memberBeans;

	// 付费权限有效期_开始时间
	@Column(name="expiry_date_begin")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date expiryDateBegin;

	// 付费权限有效期_结束时间
	@Column(name="expiry_date_end")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date expiryDateEnd;

	// 创建时间
	@Column(name="create_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	// 登录微信接口服务令牌对象
	@Transient
	private WeChatToken weChatToken;
}
