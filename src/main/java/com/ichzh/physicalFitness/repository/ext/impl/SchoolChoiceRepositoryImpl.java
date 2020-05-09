package com.ichzh.physicalFitness.repository.ext.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.ichzh.physicalFitness.conf.SelfConfig;
import com.ichzh.physicalFitness.model.SchoolChoice;
import com.ichzh.physicalFitness.repository.SchoolChoiceRepository;
import com.ichzh.physicalFitness.repository.ext.ISchoolChoiceRepositoryExt;
import com.yodoo.spring.dynamicquery.BaseRepositoryExtImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SchoolChoiceRepositoryImpl extends BaseRepositoryExtImpl implements ISchoolChoiceRepositoryExt {

	@Autowired
	private SchoolChoiceRepository schoolChoiceRepository;
	
	@Autowired RestTemplate restTemplate;
	
	@Autowired SelfConfig selfConfig;
	
	@Override
	public boolean exportSchoolForMongodb() {
		try {
			List<SchoolChoice> school = schoolChoiceRepository.findAll();
			List<Map<String,Object>> schoolMapList = new ArrayList<Map<String,Object>>();
			for (SchoolChoice s : school) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("choiceId",s.getChoiceId());
				map.put("serviceBlock",s.getServiceBlock());
				map.put("town",s.getTown());
				map.put("schoolCode",s.getSchoolCode()); 
				map.put("schoolName",s.getSchoolName());
				map.put("schoolRunningType",s.getSchoolRunningType());
				map.put("organizers",s.getOrganizers());
				map.put("address",s.getAddress());
				map.put("website",s.getWebsite());
				map.put("schoolDevelopment",s.getSchoolDevelopment());
				map.put("studentSource",s.getStudentSource());
				map.put("facultyStrength",s.getFacultyStrength());
				map.put("famousTeacherRes",s.getFamousTeacherRes());
				map.put("hardwareCondition",s.getHardwareCondition());
				map.put("yearYear",s.getYearYear());
				schoolMapList.add(map);
			}
			
			HttpHeaders requestHeaders = new HttpHeaders();
		    MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");  
		    requestHeaders.setContentType(type);
		    requestHeaders.add("Accept", MediaType.ALL_VALUE);
			
			for (Map<String, Object> map : schoolMapList) {
				HttpEntity<String> requestEntity = new HttpEntity<String>(null, requestHeaders);
			      List<Map<String,Object>> tempList = new ArrayList<>();
			      tempList.add(map);
				restTemplate.postForEntity(selfConfig.getServerUrl()+"/api/schoolchoice/addSchoolChoice?json={1}", 
						requestEntity,String.class, JSON.toJSON(tempList));
			}
			return true;
		} catch (Exception e) {
			log.error("保存问题出错："+e.getMessage()+e.fillInStackTrace());
		}
		return false;
	}

	/**
	 * 通过学校名称查询中小学
	 * @param schoolName 学校名称
	 * @return
	 */
	public List<Object> querySchoolChoinceBy(String schoolName, Integer town) {
		
		List<Object>  ret = restTemplate.postForObject(selfConfig.getServerUrl()
				+ "/api/schoolchoice/get_school_list_by_schoolname?schoolName={1}&town={2}", 
				null, List.class, schoolName, town);	
		
		return ret;
	}
	
	

}
