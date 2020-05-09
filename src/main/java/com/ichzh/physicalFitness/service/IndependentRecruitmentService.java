package com.ichzh.physicalFitness.service;

import java.util.List;

import com.ichzh.physicalFitness.model.IndependentRecruitment;

public interface IndependentRecruitmentService {

	/**
	 * 为招生范围中的学校设置其它信息.
	 * @param independentRecruitments  List<IndependentRecruitment>
	 * @param memberId  当前登录会员标识号.
	 */
	public void setOtherData(List<IndependentRecruitment> independentRecruitments, String memberId);
}
