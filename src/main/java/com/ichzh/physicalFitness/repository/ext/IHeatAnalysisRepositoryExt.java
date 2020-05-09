package com.ichzh.physicalFitness.repository.ext;

import java.util.List;

import com.ichzh.physicalFitness.model.HeatAnalysis;

public interface IHeatAnalysisRepositoryExt {

	/**
	 * 查询某个学校最新年份的热力分析
	 * @param serviceBlock
	 * @param schoolCode
	 * @return
	 */
	public List<HeatAnalysis> queryHeatAnalysisBy(Integer serviceBlock, String schoolCode);
}
