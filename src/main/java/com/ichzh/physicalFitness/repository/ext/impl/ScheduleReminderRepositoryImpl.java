package com.ichzh.physicalFitness.repository.ext.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.ichzh.physicalFitness.model.ScheduleReminder;
import com.ichzh.physicalFitness.model.UnifiedAccount;
import com.ichzh.physicalFitness.repository.ext.IScheduleReminderRepositoryExt;
import com.ichzh.physicalFitness.util.CommonUtil;
import com.yodoo.spring.dynamicquery.BaseRepositoryExtImpl;
import com.yodoo.spring.dynamicquery.ISqlElement;

public class ScheduleReminderRepositoryImpl extends BaseRepositoryExtImpl implements IScheduleReminderRepositoryExt {

	
	public boolean delScheduleReminderBy(String memberId, Integer serviceBlock, Integer town, Integer admissionMode) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("memberId", memberId);
		params.put("town", town);
		params.put("admissionMode", admissionMode);
		
		ISqlElement sqlElement = this.processSql(params, "ScheduleReminderRepositoryImpl.delScheduleReminderBy");
		
		jdbcTemplate.update(sqlElement.getSql(), sqlElement.getParams());
		return true;
	}

	public List<ScheduleReminder> queryNeedPushScheduleReminder(){
		String scheduleDate = CommonUtil.getLaterDateStr(1);
		
		Map<String, Object> params = new HashMap<>();
		params.put("scheduleDate", scheduleDate);
		
		ISqlElement sqlElement = this.processSql(params, "ScheduleReminderRepositoryImpl.queryNeedPushScheduleReminder.query");
		List<ScheduleReminder> lstScheduleReminder =  this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), 
				new BeanPropertyRowMapper<>(ScheduleReminder.class));
		
		return lstScheduleReminder;
	}
	
	public List<UnifiedAccount> queryUnifiedAccount(){
		Map<String, Object> params = new HashMap<>();
		
		ISqlElement sqlElement = this.processSql(params, "ScheduleReminderRepositoryImpl.queryUnifiedAccount.query");
		List<UnifiedAccount> lstScheduleReminder =  this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), 
				new BeanPropertyRowMapper<>(UnifiedAccount.class));
		
		return lstScheduleReminder;
	}
}
