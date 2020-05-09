package com.ichzh.physicalFitness.repository.ext.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.ichzh.physicalFitness.model.AdmissionPolicy;
import com.ichzh.physicalFitness.repository.ext.IAdmissionPolicyRepositoryExt;
import com.yodoo.spring.dynamicquery.BaseRepositoryExtImpl;
import com.yodoo.spring.dynamicquery.ISqlElement;

public class AdmissionPolicyRepositoryImpl extends BaseRepositoryExtImpl implements IAdmissionPolicyRepositoryExt {

	/**
	 * 查询入学条件
	 * @param serviceBlock   入学条件所属服务模块
	 * @param town           条件所属区
	 * @return
	 */
	public List<AdmissionPolicy> queryBy(Integer serviceBlock, Integer town) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("town", town);
		
		ISqlElement sqlElement = this.processSql(params, "AdmissionPolicyRepositoryImpl.queryBy");
		
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(AdmissionPolicy.class));
	}
}
