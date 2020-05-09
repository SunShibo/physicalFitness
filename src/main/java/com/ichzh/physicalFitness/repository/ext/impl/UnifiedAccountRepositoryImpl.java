package com.ichzh.physicalFitness.repository.ext.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.ichzh.physicalFitness.model.UnifiedAccount;
import com.ichzh.physicalFitness.repository.ext.IUnifiedAccountRepositoryExt;
import com.yodoo.spring.dynamicquery.BaseRepositoryExtImpl;
import com.yodoo.spring.dynamicquery.ISqlElement;

public class UnifiedAccountRepositoryImpl extends BaseRepositoryExtImpl implements IUnifiedAccountRepositoryExt {

	/**
	 * 根据小程序openId获取公众号openId
	 * @param wechatOpenId  小程序openId
	 * @return
	 */
	public List<UnifiedAccount> queryUnifiedAccountBy(String wechatOpenId) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("wechatOpenId", wechatOpenId);
		
		ISqlElement sqlElement = this.processSql(params, "UnifiedAccountRepositoryImpl.queryUnifiedAccountBy");
		
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(UnifiedAccount.class));
	}

	
}
