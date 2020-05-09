package com.ichzh.physicalFitness.repository.ext.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.ichzh.physicalFitness.conf.Constant;
import com.ichzh.physicalFitness.model.IndependentRecruitment;
import com.ichzh.physicalFitness.repository.ext.IIndependentRecruitmentRepositoryExt;
import com.yodoo.spring.dynamicquery.BaseRepositoryExtImpl;
import com.yodoo.spring.dynamicquery.ISqlElement;

public class IndependentRecruitmentRepositoryImpl extends BaseRepositoryExtImpl
		implements IIndependentRecruitmentRepositoryExt {

	@Override
	public List<IndependentRecruitment> queryBy(Integer serviceBlock, Integer town, Integer isCity) {
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("town", town);
		params.put("townId", town==null?110101:town);
		params.put("isCity", isCity);
		params.put("functionCode", Constant.FUNCTION_CODE_10708);
		
		ISqlElement sqlElement = this.processSql(params, "IndependentRecruitmentRepositoryImpl.queryBy.query");
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(IndependentRecruitment.class));
	}

	
	
}
