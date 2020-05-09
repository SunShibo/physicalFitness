package com.ichzh.physicalFitness.repository.ext.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.ichzh.physicalFitness.conf.Constant;
import com.ichzh.physicalFitness.dto.RangeDto;
import com.ichzh.physicalFitness.dto.SchoolDto;
import com.ichzh.physicalFitness.dto.StreetCommunityDto;
import com.ichzh.physicalFitness.model.JzdSchool;
import com.ichzh.physicalFitness.repository.ext.IJzdSchoolRepositoryExt;
import com.yodoo.spring.dynamicquery.BaseRepositoryExtImpl;
import com.yodoo.spring.dynamicquery.ISqlElement;

public class JzdSchoolRepositoryImpl extends BaseRepositoryExtImpl  implements IJzdSchoolRepositoryExt{

	/**
	 * 查询居住地对应街道的个数
	 * @param serviceBlock   居住地所属服务模块
	 * @param town           居住地所属区
	 * @return
	 */
	public int queryStreetNum(Integer serviceBlock, Integer town) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("town", town);
		
		ISqlElement sqlElement = this.processSql(params, "JzdSchoolRepositoryImpl.queryStreetNum");
		return this.jdbcTemplate.queryForObject(sqlElement.getSql(), Integer.class, sqlElement.getParams());
	}

	/**
	 * 查询居住地对应社区的个数
	 * @param serviceBlock   居住地所属服务模块
	 * @param town           居住地所属区
	 * @return
	 */
	public int queryCommunityNum(Integer serviceBlock, Integer town) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("town", town);
		
		ISqlElement sqlElement = this.processSql(params, "JzdSchoolRepositoryImpl.queryCommunityNum");
		return this.jdbcTemplate.queryForObject(sqlElement.getSql(), Integer.class, sqlElement.getParams());
	}

	/**
	 * 查询居住地对应地址的个数
	 * @param serviceBlock   居住地所属服务模块
	 * @param town           居住地所属区
	 * @return
	 */
	public int queryDetailAddressNum(Integer serviceBlock, Integer town) {
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("town", town);
		
		ISqlElement sqlElement = this.processSql(params, "JzdSchoolRepositoryImpl.queryDetailAddressNum");
		return this.jdbcTemplate.queryForObject(sqlElement.getSql(), Integer.class, sqlElement.getParams());
	}
	
	/**
	 * 查询居住地对应学校
	 * @param serviceBlock
	 * @param town
	 * @param streetName
	 * @param communityName
	 * @param detailAddress
	 * @return
	 */
	@Override
	public List<JzdSchool> queryBy(Integer serviceBlock, Integer town, String streetName, String communityName,
			String detailAddress){
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("town", town);
		params.put("streetName", streetName);
		params.put("communityName", communityName);
		params.put("detailAddress", detailAddress);
		params.put("functionCode", Constant.FUNCTION_CODE_10705);
		
		ISqlElement sqlElement = this.processSql(params, "JzdSchoolRepositoryImpl.queryBy.query");
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(JzdSchool.class));
	}

	@Override
	public List<StreetCommunityDto> queryStreetName(Integer serviceBlock, Integer town, String streetName) {
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("town", town);
		params.put("streetName", streetName);
		params.put("functionCode", Constant.FUNCTION_CODE_10705);
		
		ISqlElement sqlElement = this.processSql(params, "JzdSchoolRepositoryImpl.queryStreetName.query");
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(StreetCommunityDto.class));
	}

	@Override
	public List<StreetCommunityDto> queryCommunityName(Integer serviceBlock, Integer town, String streetName,
			String communityName) {
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("town", town);
		params.put("streetName", streetName);
		params.put("communityName", communityName);
		params.put("functionCode", Constant.FUNCTION_CODE_10705);
		
		ISqlElement sqlElement = this.processSql(params, "JzdSchoolRepositoryImpl.queryCommunityName.query");
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(StreetCommunityDto.class));
	}

	@Override
	public List<StreetCommunityDto> queryDetailAddress(Integer serviceBlock, Integer town,String streetName, String communityName,
			String detailAddress) {
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("town", town);
		params.put("streetName", streetName);
		params.put("communityName", communityName);
		params.put("detailAddress", detailAddress);
		params.put("functionCode", Constant.FUNCTION_CODE_10705);
		
		ISqlElement sqlElement = this.processSql(params, "JzdSchoolRepositoryImpl.queryDetailAddress.query");
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(StreetCommunityDto.class));
	}

	@Override
	public List<SchoolDto> queryJzdSchoolInfo(Integer serviceBlock, Integer town, String schoolName) {
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("town", town);
		params.put("schoolName", schoolName);
		params.put("functionCode", Constant.FUNCTION_CODE_10709);
		
		ISqlElement sqlElement = this.processSql(params, "JzdSchoolRepositoryImpl.queryJzdSchoolInfo.query");
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(SchoolDto.class));
	}

	@Override
	public List<RangeDto> queryStreetCommunityAddress(Integer serviceBlock, Integer town, String schoolName) {
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("town", town);
		params.put("schoolName", schoolName);
		params.put("functionCode", Constant.FUNCTION_CODE_10709);
		
		ISqlElement sqlElement = this.processSql(params, "JzdSchoolRepositoryImpl.queryStreetCommunityAddress.query");
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(RangeDto.class));
	}

	
}
