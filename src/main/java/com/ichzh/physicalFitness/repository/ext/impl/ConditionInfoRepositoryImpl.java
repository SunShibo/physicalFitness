package com.ichzh.physicalFitness.repository.ext.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.ichzh.physicalFitness.model.ConditionInfo;
import com.ichzh.physicalFitness.repository.MemberRepository;
import com.ichzh.physicalFitness.repository.ext.IConditionInfoRepositoryExt;
import com.yodoo.spring.dynamicquery.BaseRepositoryExtImpl;
import com.yodoo.spring.dynamicquery.ISqlElement;

public class ConditionInfoRepositoryImpl extends BaseRepositoryExtImpl implements IConditionInfoRepositoryExt {

	@Autowired
	private MemberRepository  memberRepository;
	
	/**
	 * 查询某个会员所有的筛选条件
	 * @param memberId   会员ID
	 * @return
	 */
	public List<ConditionInfo> queryBy(String memberId) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("memberId", memberId);
		
		ISqlElement sqlElement = this.processSql(params, "ConditionInfoRepositoryImpl.queryBy");
		
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(ConditionInfo.class));
	}

	
}
