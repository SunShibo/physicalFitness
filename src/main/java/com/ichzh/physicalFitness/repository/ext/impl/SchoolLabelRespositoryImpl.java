package com.ichzh.physicalFitness.repository.ext.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.ichzh.physicalFitness.model.SchoolLabel;
import com.ichzh.physicalFitness.repository.ext.ISchoolLabelRespositoryExt;
import com.yodoo.spring.dynamicquery.BaseRepositoryExtImpl;
import com.yodoo.spring.dynamicquery.ISqlElement;

public class SchoolLabelRespositoryImpl extends BaseRepositoryExtImpl implements ISchoolLabelRespositoryExt {

	/**
	 * 根据学校代码查询学校标签：按顺序号升序排列
	 * @param schoolCode  学校代码
	 * @return List<SchoolLabel>.
	 */
	public List<SchoolLabel> querySchoolLabelBy(String schoolCode) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("schoolCode", schoolCode);
		
		ISqlElement sqlElement = this.processSql(params, "SchoolLabelRespositoryImpl.querySchoolLabelBy");
		
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(SchoolLabel.class));
	}

	
}
