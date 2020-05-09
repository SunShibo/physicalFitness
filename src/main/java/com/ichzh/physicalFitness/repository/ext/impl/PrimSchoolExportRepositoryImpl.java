package com.ichzh.physicalFitness.repository.ext.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.ichzh.physicalFitness.dto.SchoolExportDto;
import com.ichzh.physicalFitness.model.JzdSchool;
import com.ichzh.physicalFitness.repository.ext.IPrimSchoolExportRepositoryExt;
import com.yodoo.spring.dynamicquery.BaseRepositoryExtImpl;
import com.yodoo.spring.dynamicquery.ISqlElement;

public class PrimSchoolExportRepositoryImpl extends BaseRepositoryExtImpl implements IPrimSchoolExportRepositoryExt {

	/**
	 * 查询某个小学出口数据最近三年是哪几年
	 * @param schoolCode  小学学校代码.
	 * @return List<Integer>.
	 */
	public List<Integer> queryYearOfPrimSchoolExportLatestThree(String schoolCode) {
		
		
		Map<String, Object> params = new HashMap<>();
		params.put("schoolCode", schoolCode);
		
		ISqlElement sqlElement = this.processSql(params, "PrimSchoolExportRepositoryImpl.queryYearOfPrimSchoolExportLatestThree");
		
		return jdbcTemplate.queryForList(sqlElement.getSql(), Integer.class, sqlElement.getParams());
	}

	/**
	 * 查询其它出口学校列表数据(占比小于5%)
	 * @param schoolCode  小学学校代码
	 * @param year 出口数据所属年份
	 * @return List<SchoolExportDto>.
	 */
	public List<SchoolExportDto> queryOtherExportSchoolListData(String schoolCode, Integer year) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("schoolCode", schoolCode);
		params.put("year", year);
		
		ISqlElement sqlElement = this.processSql(params, "PrimSchoolExportRepositoryImpl.queryOtherExportSchoolListData");
		
		return jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(SchoolExportDto.class));
	}
	
	

	
}
