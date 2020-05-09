package com.ichzh.physicalFitness.repository.ext.impl;

import java.util.HashMap;
import java.util.Map;

import com.ichzh.physicalFitness.repository.ext.ISchoolCollectionRepositoryExt;
import com.yodoo.spring.dynamicquery.BaseRepositoryExtImpl;
import com.yodoo.spring.dynamicquery.ISqlElement;

public class SchoolCollectionRepositoryImpl extends BaseRepositoryExtImpl implements ISchoolCollectionRepositoryExt {

	
	public void deleteByServiceBlockAndMemberIdAndSchoolId(Integer serviceBlock, String memberId, Integer schoolId) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("memberId", memberId);
		params.put("schoolId", schoolId);
		
		ISqlElement sqlElement = this.processSql(params, "SchoolCollectionRepositoryImpl.deleteByServiceBlockAndMemberIdAndSchoolId");
		
		jdbcTemplate.update(sqlElement.getSql(), sqlElement.getParams());
	}

	
}
