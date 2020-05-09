package com.ichzh.physicalFitness.repository.ext.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.ichzh.physicalFitness.conf.Constant;
import com.ichzh.physicalFitness.conf.SelfConfig;
import com.ichzh.physicalFitness.dto.StudentStatusDto;
import com.ichzh.physicalFitness.model.NurserySchool;
import com.ichzh.physicalFitness.repository.ext.INurserySchoolRepositoryExt;
import com.yodoo.spring.dynamicquery.BaseRepositoryExtImpl;
import com.yodoo.spring.dynamicquery.ISqlElement;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
@Slf4j
public class NurserySchoolRepositoryImpl extends BaseRepositoryExtImpl implements INurserySchoolRepositoryExt {

	@Autowired RestTemplate restTemplate;
	
	@Autowired SelfConfig selfConfig;
	
	@Override
	public boolean saveSchoolInfo(List<Map<String, Object>> schoolMapList) {
		
		try {
			HttpHeaders requestHeaders = new HttpHeaders();
		    MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");  
		    requestHeaders.setContentType(type);
		    requestHeaders.add("Accept", MediaType.ALL_VALUE);
			
			for (Map<String, Object> map : schoolMapList) {
				HttpEntity<String> requestEntity = new HttpEntity<String>(null, requestHeaders);
			      List<Map<String,Object>> tempList = new ArrayList<>();
			      tempList.add(map);
				restTemplate.postForEntity(selfConfig.getServerUrl()+"/api/school/addSchoolInfo?json={1}", 
						requestEntity,String.class, JSON.toJSON(tempList));
			}
			return true;
		} catch (Exception e) {
			log.error("保存问题出错："+e.getMessage()+e.fillInStackTrace());
		}
		return false;
	}

	@Override
	public List<StudentStatusDto> queryOrganizers(Integer serviceBlock, Integer town) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("town", town);
		params.put("functionCode", Constant.FUNCTION_CODE_10711);
		
		ISqlElement sqlElement = this.processSql(params, "NurserySchoolRepositoryImpl.queryOrganizers.query");
		
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(StudentStatusDto.class));
	}

	/**
	 * 查询幼儿园列表
	 * @param serviceBlock  入学条件所属服务模块
	 * @param town          条件所属区
	 * @param organizers    举办者
	 * @param isModel       是否示范
	 * @param isInclusive   是否民办普惠
	 * @return 
	 */
	@Override
	public List<NurserySchool> queryNurserySchool(Integer serviceBlock, Integer town, Integer organizers,
			Integer isModel, Integer isInclusive, String memberId) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("town", town);
		params.put("organizers", organizers);
		params.put("isModel", isModel);
		params.put("isInclusive", isInclusive);
		params.put("functionCode", Constant.FUNCTION_CODE_10711);
		params.put("memberId", memberId);
		
		ISqlElement sqlElement = this.processSql(params, "NurserySchoolRepositoryImpl.queryNurserySchool.query");
		
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(NurserySchool.class));
	}

	@Override
	public List<Map> queryNurserySchoolMapRange(Integer serviceBlock, Integer town, Double latitude, Double longitude,
			Integer maxDistance, Integer schoolType) {
		try {
			if(maxDistance==null) {//为空时默认5
				maxDistance = 5;
			}
			HttpHeaders requestHeaders = new HttpHeaders();
		    MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");  
		    requestHeaders.setContentType(type);
		    requestHeaders.add("Accept", MediaType.ALL_VALUE);
		    HttpEntity<String> requestEntity = new HttpEntity<String>(null, requestHeaders);
		    ResponseEntity<String> responseEntity = restTemplate.postForEntity(selfConfig.getServerUrl()+"/api/school/querySchoolInfo?longitude={1}&latitude={2}&maxDistance={3}&town={4}&schoolType={5}", 
					requestEntity,String.class, longitude,latitude,maxDistance,town, schoolType);
		    JSONArray jsonArr = JSONArray.fromObject(responseEntity.getBody());
			List<Map> schoolList = JSONArray.toList(jsonArr, Map.class);
			return schoolList;
		} catch (Exception e) {
			log.error("查询mongodb数据出错："+e.getMessage()+e.fillInStackTrace());
		}
		return null;
	}

	@Override
	public List<Object> queryNurserySchoolBy(String schoolName, Integer town) {
		
		List<Object>  ret = restTemplate.postForObject(selfConfig.getServerUrl()+"/api/school/get_school_list_by_schoolname?schoolName={1}&town={2}", null, List.class, schoolName, town);
		
		return ret;
	}
	
	

}
