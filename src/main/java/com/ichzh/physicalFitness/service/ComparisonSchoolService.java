package com.ichzh.physicalFitness.service;

import java.util.List;

import com.ichzh.physicalFitness.model.ComparisonSchool;

public interface ComparisonSchoolService {

	/**
	 * 保存对比学校信息.
	 * @param memeberId  会员标识号.
	 * @param schoolId   学校标识号.
	 * @param schoolType 学校类型：1 中小学 2 幼儿园
	 * @param orderNum 显示序号
	 * @return
	 */
	public boolean writeComSchool(String memberId, Integer schoolId, Integer schoolType, Integer orderNum);
	
	/**
	 * 设置对比校的热力值
	 * @param ComparisonSchools List<ComparisonSchool>.
	 */
	public void setSchoolHeatingPowerValue(List<ComparisonSchool> ComparisonSchools);
	
}
