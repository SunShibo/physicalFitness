package com.ichzh.physicalFitness.repository.ext.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.ichzh.physicalFitness.model.AdmissionMode;
import com.ichzh.physicalFitness.repository.ext.IAdmissionModeRepositoryExt;
import com.yodoo.spring.dynamicquery.BaseRepositoryExtImpl;
import com.yodoo.spring.dynamicquery.ISqlElement;

public class AdmissionModeRepositoryImpl extends BaseRepositoryExtImpl implements IAdmissionModeRepositoryExt {

	/**
	 * 查询入学条件
	 * @param serviceBlock   入学条件所属服务模块
	 * @param town           条件所属区
	 * @return
	 */
	public List<AdmissionMode> queryBy(Integer serviceBlock, Integer town) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("town", town);
		
		ISqlElement sqlElement = this.processSql(params, "AdmissionModeRepositoryImpl.queryBy");
		
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(AdmissionMode.class));
	}
}
