package com.ichzh.physicalFitness.repository.ext.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.ichzh.physicalFitness.model.HeatAnalysis;
import com.ichzh.physicalFitness.repository.ext.IHeatAnalysisRepositoryExt;
import com.yodoo.spring.dynamicquery.BaseRepositoryExtImpl;
import com.yodoo.spring.dynamicquery.ISqlElement;

public class HeatAnalysisRepositoryImpl extends BaseRepositoryExtImpl implements IHeatAnalysisRepositoryExt {

	/**
	 * 查询某个学校最新年份的热力分析
	 * @param serviceBlock
	 * @param schoolCode
	 * @return
	 */
	public List<HeatAnalysis> queryHeatAnalysisBy(Integer serviceBlock, String schoolCode) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("schoolCode", schoolCode);
		
		ISqlElement sqlElement = this.processSql(params, "HeatAnalysisRepositoryImpl.queryHeatAnalysisBy");
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(HeatAnalysis.class));
	}

	
}
