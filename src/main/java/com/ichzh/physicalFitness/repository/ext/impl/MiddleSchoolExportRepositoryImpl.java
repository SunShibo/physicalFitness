package com.ichzh.physicalFitness.repository.ext.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.ichzh.physicalFitness.dto.SchoolExportDto;
import com.ichzh.physicalFitness.repository.ext.IMiddleSchoolExportRepositoryExt;
import com.yodoo.spring.dynamicquery.BaseRepositoryExtImpl;
import com.yodoo.spring.dynamicquery.ISqlElement;

public class MiddleSchoolExportRepositoryImpl extends BaseRepositoryExtImpl implements IMiddleSchoolExportRepositoryExt {

	/**
	 * 查询某个初中出口数据最近三年是哪几年
	 * @param schoolCode  初中学校代码.
	 * @return List<Integer>.
	 */
	public List<Integer> queryYearOfMiddleSchoolExportLatestThree(String schoolCode) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("schoolCode", schoolCode);
		
		ISqlElement sqlElement = this.processSql(params, "MiddleSchoolExportRepositoryImpl.queryYearOfMiddleSchoolExportLatestThree");
		
		return jdbcTemplate.queryForList(sqlElement.getSql(), Integer.class, sqlElement.getParams());
	}

	/**
	 * 查询其它出口学校列表数据
	 * @param schoolCode  初中学校代码
	 * @param year 出口数据所属年份
	 * @return List<SchoolExportDto>.
	 */
	public List<SchoolExportDto> queryOtherExportSchoolListData(String schoolCode, Integer year) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("schoolCode", schoolCode);
		params.put("year", year);
		
		ISqlElement sqlElement = this.processSql(params, "MiddleSchoolExportRepositoryImpl.queryOtherExportSchoolListData");
		
		return jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(SchoolExportDto.class));
	}

	
}
