package com.ichzh.physicalFitness.service;

import java.util.List;

import com.ichzh.physicalFitness.model.SchoolChoice;

public interface SchoolChoiceService {

	public void setOtherData(String memberId, SchoolChoice schoolChoice);
	
	/**
	 * 将学校简介的五类信息拆分为多个段落
	 * @param schoolChoice
	 */
	public void splitSchoolProfile(SchoolChoice schoolChoice);
	/**
	 * 设置学校对应的热力值
	 * @param schoolChoice
	 */
	public void setSchoolHeatingPowerValue(SchoolChoice schoolChoice);
	
	/**
	 * 设置当前学校所在区，每个热力等级对应的学校
	 * @param schoolChoice
	 */
	public void setLevelSchoolHeatingPower(SchoolChoice schoolChoice, String memberId);
	
	/**
	 * 设置学校标签
	 * @param schoolChoice
	 */
	public void setSchoolLabel(SchoolChoice schoolChoice);
	/**
	  * 设置办学类型名称和描述
	 * @param schoolChoice
	 */
	public void setSchoolRunningTypeData(SchoolChoice schoolChoice);
	
	
	/**
	 * 设置学校对应的热力值
	 * @param schoolChoices
	 */
	public void setSchoolHeatingPowerValue(List<SchoolChoice> schoolChoices);
	
	/**
	 * 根据具体数据情况，设置哪些模块是否需要显示.
	 * @param schoolChoice
	 */
	public void setModelIfShow(SchoolChoice schoolChoice);
	/**
	 * 将历史沿革拆分为多条数据集合
	 * @param schoolChoice
	 */
	public void setHistoryEvolution(SchoolChoice schoolChoice);
	/**
	 * 设置热力分析数据
	 * @param schoolChoice
	 */
	public void setHeatAnalysises(SchoolChoice schoolChoice);
	/**
	 * 设置趋势值
	 * @param schoolChoice
	 */
	public void setTrend(SchoolChoice schoolChoice);
	/**
	 * 设置排名(按区)
	 * @param schoolChoice
	 */
	public void setRanking(SchoolChoice schoolChoice);
	/**
	 * 设置排名(按片区)
	 * @param schoolChoice
	 */
	public void setRankingArea(SchoolChoice schoolChoice);
}
