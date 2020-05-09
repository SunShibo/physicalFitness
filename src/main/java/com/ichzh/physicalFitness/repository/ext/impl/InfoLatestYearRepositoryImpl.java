package com.ichzh.physicalFitness.repository.ext.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.ichzh.physicalFitness.model.InfoLatestYear;
import com.ichzh.physicalFitness.repository.ext.IInfoLatestYearRepositoryExt;
import com.yodoo.spring.dynamicquery.BaseRepositoryExtImpl;
import com.yodoo.spring.dynamicquery.ISqlElement;

public class InfoLatestYearRepositoryImpl extends BaseRepositoryExtImpl implements IInfoLatestYearRepositoryExt{

	/**
	 * 查询每个功能编号信息更新年度信息
	 * @param serviceBlock   服务模块
	 * @param town           区
	 * @return
	 */
	public List<InfoLatestYear> queryBy(Integer serviceBlock, Integer town) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("town", town);
		
		ISqlElement sqlElement = this.processSql(params, "InfoLatestYearRepositoryExtImpl.queryBy");
		
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(InfoLatestYear.class));
	}

	
}
