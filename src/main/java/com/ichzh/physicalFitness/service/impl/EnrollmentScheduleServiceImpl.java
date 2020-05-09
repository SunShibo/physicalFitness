package com.ichzh.physicalFitness.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.model.EnrollmentSchedule;
import com.ichzh.physicalFitness.repository.EnrollmentScheduleRepository;
import com.ichzh.physicalFitness.service.EnrollmentScheduleService;
import com.ichzh.physicalFitness.service.ICacheApplicationService;
import com.ichzh.physicalFitness.util.CommonUtil;

@Service("enrollmentScheduleService")
public class EnrollmentScheduleServiceImpl implements EnrollmentScheduleService {

	@Autowired
	EnrollmentScheduleRepository  enrollmentScheduleRepository;
	@Autowired
	ICacheApplicationService cacheApplicationService;
	/**
	 * 查询给定日期的日程安排.
	 * @param serviceBlock  学段  10001 幼儿园  10002 小学  10003 初中
	 * @param town  区
	 * @param weekNum 0: 表示查询当前周的日程；1：表示查询下一周的日程；-1：表示查询上一周的日程.
	 * @return
	 */
	public List<EnrollmentSchedule> queryEnroolmentScheduleBy(Integer serviceBlock, Integer town, Integer weekNum,
			Integer admissionMode) {
		
		List<EnrollmentSchedule> retSchedules = new ArrayList<EnrollmentSchedule>();
		
		//指定周对应的天(第一天是周日，最后一天是周六)
		List<Date> dates = CommonUtil.getDayOfBeforeOrAfterWeek(weekNum);
		for (Date oneDate : dates) {
			EnrollmentSchedule oneEnrollmentSchedule = cacheApplicationService.getEnrollmentScheduleBy(serviceBlock, town, 
					oneDate, admissionMode);
			if (oneEnrollmentSchedule == null) {
				oneEnrollmentSchedule = new EnrollmentSchedule();
				oneEnrollmentSchedule.setScheduleDate(oneDate);
			}
			retSchedules.add(oneEnrollmentSchedule);
		}
		
		//设置哪天是当天
		if (retSchedules != null && retSchedules.size() > 0) {
			for (EnrollmentSchedule oneSchedule : retSchedules) {
				
				if (CommonUtil.DateToDateString(oneSchedule.getScheduleDate()).equals(CommonUtil.DateToDateString(new Date()))) {
					oneSchedule.setIsToday(1);
				}else {
					oneSchedule.setIsToday(0);
				}
				
				if (StringUtils.isEmpty(oneSchedule.getManagerDoSomething())) {
					oneSchedule.setManagerDoSomething("无日程安排");
				}
				if (StringUtils.isEmpty(oneSchedule.getParentDoSomething())) {
					oneSchedule.setParentDoSomething("无日程安排");
				}
				oneSchedule.setChineseMonth(CommonUtil.getMonth(oneSchedule.getScheduleDate()));
			}
		}
		return retSchedules;
	}
	
	
	/**
	 * 查询给定日期的日程安排.
	 * @param serviceBlock  学段  10001 幼儿园  10002 小学  10003 初中
	 * @param town  区
	 * @return
	 */
	public List<EnrollmentSchedule> queryEnroolmentScheduleBy(Integer serviceBlock, Integer town, Integer admissionMode) {
		
		List<EnrollmentSchedule> enrollmentSchedules = cacheApplicationService.getEnrollmentScheduleBy(serviceBlock, town, 
				admissionMode);
		if (enrollmentSchedules != null && enrollmentSchedules.size() > 0) {
			for (EnrollmentSchedule oneEnrollmentSchedule : enrollmentSchedules) {
				if (CommonUtil.DateToDateString(oneEnrollmentSchedule.getScheduleDate()).equals(CommonUtil.DateToDateString(new Date()))) {
					oneEnrollmentSchedule.setIsToday(0);
				}else if(CommonUtil.DateToDateString(oneEnrollmentSchedule.getScheduleDate()).compareTo(CommonUtil.DateToDateString(new Date())) < 0){
					oneEnrollmentSchedule.setIsToday(-1);
				}else {
					oneEnrollmentSchedule.setIsToday(1);
				}
			}
		}
		
		return enrollmentSchedules;
	}

	
	public List<Map<String, Object>> queryEnrollmentScheduleAdmissionMode(Integer serviceBlock, Integer town){
		
		List<EnrollmentSchedule> lstEnrollmentSchedule = cacheApplicationService.getEnrollmentScheduleBy(serviceBlock, town);
		List<Map<String, Object>> lstDictInfo = new ArrayList<>();
		if(!CollectionUtils.isEmpty(lstEnrollmentSchedule)) {
			List<Integer> lstDictId = new ArrayList<>();		
			for(EnrollmentSchedule enrollmentSchedule : lstEnrollmentSchedule) {
				if(!lstDictId.contains(enrollmentSchedule.getAdmissionMode())) {
					if(enrollmentSchedule.getAdmissionMode() != null) {
						Map<String, Object> map = new HashMap<>();
						
						map.put("admissionMode", enrollmentSchedule.getAdmissionMode());
						map.put("admissionModeValue", cacheApplicationService.getDictName(
								enrollmentSchedule.getAdmissionMode()));
						
						lstDictId.add(enrollmentSchedule.getAdmissionMode());
						lstDictInfo.add(map);
					}
				}
			}
		}
		return lstDictInfo;
	}
	
}
