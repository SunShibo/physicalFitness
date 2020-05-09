package com.ichzh.physicalFitness.web.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ichzh.physicalFitness.model.NurserySchool;
import com.ichzh.physicalFitness.repository.AidedAllocateRangeRepository;
import com.ichzh.physicalFitness.repository.NurserySchoolRepository;
import com.ichzh.physicalFitness.repository.SchoolChoiceRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping({"/notwxchart"})
public class NurserySchoolExportApi {

	@Autowired
	private NurserySchoolRepository nurserySchoolRepository;
	
	@Autowired
	private SchoolChoiceRepository schoolChoiceRepository;
	
	@Autowired
	private AidedAllocateRangeRepository aidedAllocateRangeRepository;
	
	/**
	 * 查询入学方式
	 * @param serviceBlock    入学条件所属服务模块         
	 * @param town            条件所属区
	 * @return
	 */
	@RequestMapping(value="/export_for_mongodb", method= {RequestMethod.POST,RequestMethod.GET})
	public void exportForMongodb() {
		try
		{
			List<NurserySchool> school = nurserySchoolRepository.findAll();
			List<Map<String,Object>> schoolMapList = new ArrayList<Map<String,Object>>();
			for (NurserySchool s : school) {
				Map<String,Object> map = new HashMap<String,Object>();
//				map.put("nursery_school_id", s.getNurserySchoolId());
//				map.put("school_code", s.getSchoolCode());
//				map.put("school_name", s.getSchoolName());
//				map.put("town", s.getTown());
//				map.put("latitude", s.getDimension());
//				map.put("longitude", s.getLongitude());
				map.put("nurserySchoolId",s.getNurserySchoolId());
				map.put("schoolCode",s.getSchoolCode());
				map.put("schoolName",s.getSchoolName());
				map.put("isPublic",s.getIsPublic());
				map.put("isPrivate",s.getIsPrivate());
				map.put("isModel",s.getIsModel());
				map.put("isInclusive",s.getIsInclusive());
				map.put("longitude",s.getLongitude());
				map.put("dimension",s.getDimension());
				map.put("organizers",s.getOrganizers());
				map.put("town",s.getTown());
				map.put("websiteUrl",s.getWebsiteUrl());
				map.put("address",s.getAddress());
				map.put("campusScale",s.getCampusScale());
				map.put("campusDevelopment",s.getCampusDevelopment());
				map.put("facultyStrength",s.getFacultyStrength());
				map.put("hardwareCondition",s.getHardwareCondition());
				map.put("schoolProfile",s.getSchoolProfile());
				map.put("yearYear",s.getYearYear());
				schoolMapList.add(map);
			}
			nurserySchoolRepository.saveSchoolInfo(schoolMapList);
		}catch(Exception ex) {
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
	}
	
	/**
	 * 中小学导入数据到mongodb库
	 * @return
	 */
	@RequestMapping(value="/export_school_choice_for_mongodb", method= {RequestMethod.POST,RequestMethod.GET})
	public String exportSchoolChoiceForMongodb() {
		try
		{
			schoolChoiceRepository.exportSchoolForMongodb();
			return "ok";
		}catch(Exception ex) {
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		return "no";
	}
	
//	@RequestMapping(value="/batchUpdateTable", method= {RequestMethod.POST,RequestMethod.GET})
//	public String batchUpdateTable() {
//		
//		return aidedAllocateRangeRepository.batchUpdateTable();
//	}
}
