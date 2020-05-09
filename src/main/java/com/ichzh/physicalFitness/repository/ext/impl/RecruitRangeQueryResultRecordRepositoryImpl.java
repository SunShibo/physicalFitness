package com.ichzh.physicalFitness.repository.ext.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.ichzh.physicalFitness.model.RecruitRangeQueryResultRecord;
import com.ichzh.physicalFitness.repository.ext.IRecruitRangeQueryResultRecordRepositoryExt;
import com.yodoo.spring.dynamicquery.BaseRepositoryExtImpl;
import com.yodoo.spring.dynamicquery.ISqlElement;

public class RecruitRangeQueryResultRecordRepositoryImpl extends BaseRepositoryExtImpl implements IRecruitRangeQueryResultRecordRepositoryExt {

	/**
	 * 查询某个会员某一天的查询记录
	 * @param memberId   会员标识号.
	 * @param recordTime 查询时间.
	 * @return  List<RecruitRangeQueryResultRecord>.
	 */
	public List<RecruitRangeQueryResultRecord> queryByMemberIdAndRecordTime(String memberId, Date recordTime) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("memberId", memberId);
		params.put("recordTime", DateFormatUtils.format(recordTime, "yyyy-MM-dd"));
		
		ISqlElement sqlElement = this.processSql(params, "RecruitRangeQueryResultRecordRepositoryImpl.queryByMemberIdAndRecordTime");
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(RecruitRangeQueryResultRecord.class));
	}

	
}
