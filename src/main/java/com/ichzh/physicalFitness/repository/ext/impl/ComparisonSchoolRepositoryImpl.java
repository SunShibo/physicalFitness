package com.ichzh.physicalFitness.repository.ext.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.ichzh.physicalFitness.model.ComparisonSchool;
import com.ichzh.physicalFitness.model.SchoolChoice;
import com.ichzh.physicalFitness.repository.ext.IComparisonSchoolRepositoryExt;
import com.yodoo.spring.dynamicquery.BaseRepositoryExtImpl;
import com.yodoo.spring.dynamicquery.ISqlElement;

public class ComparisonSchoolRepositoryImpl extends BaseRepositoryExtImpl implements IComparisonSchoolRepositoryExt {

	/**
	 * 查询某个会员的对比校.
	 * @param memberId  会员标识号.
	 * @return
	 */
	public List<SchoolChoice> queryComparisonSchoolBy(String memberId) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("memberId", memberId);
		
		ISqlElement sqlElement = this.processSql(params, "ComparisonSchoolRepositoryImpl.queryBy");
		
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(SchoolChoice.class));
	}

	/**
	 * 删除对比校
	 * @param memberId    会员标识号.
	 * @param schoolType  学校类型.  1： 中小学   2：幼儿园
	 * @param schoolId    幼儿园或中小学标识号.
	 */
	public void deleteBy(String memberId, Integer schoolType, Integer schoolId) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("memberId", memberId);
		params.put("schoolType", schoolType);
		params.put("schoolId", schoolId);
		
		ISqlElement sqlElement = this.processSql(params, "ComparisonSchoolRepositoryImpl.deleteBy");
		this.jdbcTemplate.update(sqlElement.getSql(), sqlElement.getParams());
	}

	/**
	 * 将当前会员收藏学校的显示序号增加1（当前学校除外）
	 * @param mermerId  会员标识号.
	 * @param schoolId  当前收藏的学校.
	 * @return
	 */
	public void updateOtherOrderNum(String mermerId, Integer schoolId) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("memberId", mermerId);
		params.put("schoolId", schoolId);
		
		ISqlElement sqlElement = this.processSql(params, "ComparisonSchoolRepositoryImpl.updateOtherOrderNum");
		this.jdbcTemplate.update(sqlElement.getSql(), sqlElement.getParams());
	}

	
}
