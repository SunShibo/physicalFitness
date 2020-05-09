package com.ichzh.physicalFitness.service;

import com.ichzh.physicalFitness.model.NurserySchool;
import com.ichzh.physicalFitness.model.SchoolChoice;

public interface NurserySchoolService {

	/**
	 * 将学校简介的五类信息拆分为多个段落
	 * @param nurserySchool
	 */
	public void splitSchoolProfile(NurserySchool nurserySchool);
	
	/**
	 * 设置学校标签
	 * @param schoolChoice
	 */
	public void setSchoolLabel(NurserySchool nurserySchool);
	/**
	 * 设置收藏状态
	 * @param memberId
	 * @param nurserySchool
	 */
	public void setColStatus(String memberId, NurserySchool nurserySchool);
	
	/**
	  * 设置举办者名称和描述
	 * @param nurserySchool
	 */
	public void setSchoolRunningTypeData(NurserySchool nurserySchool);
}
