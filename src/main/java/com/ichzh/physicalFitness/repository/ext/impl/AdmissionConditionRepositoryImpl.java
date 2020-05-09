package com.ichzh.physicalFitness.repository.ext.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.alibaba.druid.Constants;
import com.ichzh.physicalFitness.conf.Constant;
import com.ichzh.physicalFitness.dto.StudentStatusDto;
import com.ichzh.physicalFitness.model.AdmissionCondition;
import com.ichzh.physicalFitness.repository.ext.IAdmissionConditionRepositoryExt;
import com.yodoo.spring.dynamicquery.BaseRepositoryExtImpl;
import com.yodoo.spring.dynamicquery.ISqlElement;

public class AdmissionConditionRepositoryImpl extends BaseRepositoryExtImpl implements IAdmissionConditionRepositoryExt {

	/**
	 * 查询入学条件
	 * @param serviceBlock   入学条件所属服务模块
	 * @param town           条件所属区
	 * @param studentStatus  标签_学籍
	 * @param householdRegistration 标签_户籍
	 * @param residence  标签_居住
	 * @return
	 */
	public List<AdmissionCondition> queryBy(Integer serviceBlock, Integer town, Integer studentStatus,
			Integer householdRegistration, Integer residence) {
		
		
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("town", town);
		params.put("studentStatus", studentStatus);
		params.put("householdRegistration", householdRegistration);
		params.put("residence", residence);
		
		//上小学
		if (serviceBlock.compareTo(Constant.DICT_ID_10002) == 0) {
			params.put("studentStatus", null);
			if (householdRegistration == null) {
				params.put("householdRegistration", Constant.DICT_ID_10000);
			}
			if (residence == null) {
				params.put("residence", Constant.DICT_ID_10000);
			}
		}
		//上中学
		if (serviceBlock.compareTo(Constant.DICT_ID_10003) == 0) {
			if (studentStatus == null) {
				params.put("studentStatus", Constant.DICT_ID_10000);
			}
			if (householdRegistration == null) {
				params.put("householdRegistration", Constant.DICT_ID_10000);
			}
			if (residence == null) {
				params.put("residence", Constant.DICT_ID_10000);
			}
		}
		
		
		ISqlElement sqlElement = this.processSql(params, "AdmissionConditionRepositoryImpl.queryBy");
		
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(AdmissionCondition.class));
	}

	/**
	 * 查询搜索条件中的学籍筛选数据
	 * @param serviceBlock   入学条件所属服务模块
	 * @param town           条件所属区
	 * @return
	 */
	public List<StudentStatusDto> queryStudentStatus(Integer serviceBlock, Integer town) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("town", town);
		
		ISqlElement sqlElement = this.processSql(params, "AdmissionConditionRepositoryImpl.queryStudentStatus");
		
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(StudentStatusDto.class));
	}

	/**
	 * 查询搜索条件中的户籍筛选数据
	 * @param serviceBlock   入学条件所属服务模块
	 * @param town           条件所属区
	 * @param studentStatus  标签_学籍
	 * @return
	 */
	public List<StudentStatusDto> queryHouseholdRegistration(Integer serviceBlock, Integer town,
			Integer studentStatus) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("town", town);
		params.put("studentStatus", studentStatus);
		
		ISqlElement sqlElement = this.processSql(params, "AdmissionConditionRepositoryImpl.queryHouseholdRegistration");
		
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(StudentStatusDto.class));
	}

	/**
	 * 查询搜索条件中的居住地筛选数据
	 * @param serviceBlock   入学条件所属服务模块
	 * @param town           条件所属区
	 * @param studentStatus  标签_学籍
	 * @param householdRegistration 标签_户籍
	 * @return
	 */
	public List<StudentStatusDto> queryResidence(Integer serviceBlock, Integer town, Integer studentStatus,
			Integer householdRegistration) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("town", town);
		params.put("studentStatus", studentStatus);
		params.put("householdRegistration", householdRegistration);
		
		ISqlElement sqlElement = this.processSql(params, "AdmissionConditionRepositoryImpl.queryResidence");
		
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(StudentStatusDto.class));
	}

	
	
}
