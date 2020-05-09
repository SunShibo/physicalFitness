package com.ichzh.physicalFitness.repository.ext.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.ichzh.physicalFitness.model.AdmissionTime;
import com.ichzh.physicalFitness.repository.ext.IAdmissionTimeRepositoryExt;
import com.yodoo.spring.dynamicquery.BaseRepositoryExtImpl;
import com.yodoo.spring.dynamicquery.ISqlElement;

public class AdmissionTimeRepositoryImpl extends BaseRepositoryExtImpl  implements IAdmissionTimeRepositoryExt {

	/**
	 * 查询入学时间
	 * @param serviceBlock   入学时间所属服务模块
	 * @param town           条件所属区
	 * @return
	 */
	public List<AdmissionTime> queryBy(Integer serviceBlock, Integer town) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("town", town);
		
		ISqlElement sqlElement = this.processSql(params, "AdmissionTimeRepositoryImpl.queryBy");
		
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(AdmissionTime.class));
	}
}
