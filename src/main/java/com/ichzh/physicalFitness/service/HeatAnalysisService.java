package com.ichzh.physicalFitness.service;

import java.util.List;

import com.ichzh.physicalFitness.model.HeatAnalysis;

public interface HeatAnalysisService {

	/**
	 * 查询某个学校最新年份的热力分析
	 * @param serviceBlock
	 * @param schoolCode
	 * @return
	 */
	public List<HeatAnalysis> queryHeatAnalysisBy(Integer serviceBlock, String schoolCode);
	/**
	 * 设置占比等级和入学方式名称
	 * @param heatAnalysis
	 */
	public void setOtherData(HeatAnalysis  heatAnalysis);
}
