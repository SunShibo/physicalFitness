package com.ichzh.physicalFitness.repository.ext.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.ichzh.physicalFitness.conf.Constant;
import com.ichzh.physicalFitness.model.EnrollmentSchedule;
import com.ichzh.physicalFitness.repository.ext.IEnrollmentScheduleRepositoryExt;
import com.yodoo.spring.dynamicquery.BaseRepositoryExtImpl;
import com.yodoo.spring.dynamicquery.ISqlElement;

public class EnrollmentScheduleRepositoryImpl extends BaseRepositoryExtImpl implements IEnrollmentScheduleRepositoryExt {

	/**
	 * 查询给定日期的日程安排.
	 * @param serviceBlock  学段  10001 幼儿园  10002 小学  10003 初中
	 * @param town  区
	 * @param dates 日期集合.
	 * @return
	 */
	public List<EnrollmentSchedule> queryEnroolmentScheduleBy(Integer serviceBlock, Integer town, List<String> dates) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("town", town);
		params.put("dates", dates);
		params.put("functionCode", Constant.FUNCTION_CODE_10715);
		
		ISqlElement sqlElement = this.processSql(params, "EnrollmentScheduleRepositoryImpl.queryEnroolmentScheduleBy");
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(EnrollmentSchedule.class));
	}

	/**
	 * 根据公众号openId查询该公众号openId对应会员订阅的日程.
	 * @param gzhOpenId       公众号openId
	 * @param schedule_date   日程日期.
	 * @return
	 */
	public List<EnrollmentSchedule> queryEnrollmentScheduleByOpenId(String gzhOpenId, String schedule_date) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("gzhOpenId", gzhOpenId);
		params.put("scheduleDate", schedule_date);
		
		ISqlElement sqlElement = this.processSql(params, "EnrollmentScheduleRepositoryImpl.queryEnrollmentScheduleByOpenId");
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(EnrollmentSchedule.class));
	}

	
	
}
