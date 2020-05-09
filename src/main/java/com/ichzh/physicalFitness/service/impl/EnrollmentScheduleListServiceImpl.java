package com.ichzh.physicalFitness.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.model.EnrollmentScheduleList;
import com.ichzh.physicalFitness.service.EnrollmentScheduleListService;
import com.ichzh.physicalFitness.service.ICacheApplicationService;
import com.ichzh.physicalFitness.util.CommonUtil;

@Service("enrollmentScheduleListService")
public class EnrollmentScheduleListServiceImpl implements  EnrollmentScheduleListService{

	@Autowired
	ICacheApplicationService cacheApplicationService;
	
	public List<EnrollmentScheduleList> queryEnroolmentScheduleListBy(Integer serviceBlock, Integer town,
			Integer admissionMode) {
		
		List<EnrollmentScheduleList> enrollmentSchedules = cacheApplicationService.getEnrollmentScheduleListBy(serviceBlock, town, 
				admissionMode);
		if (enrollmentSchedules != null && enrollmentSchedules.size() > 0) {
			for (EnrollmentScheduleList oneEnrollmentSchedule : enrollmentSchedules) {
				
				if (CommonUtil.DateToDateString(new Date()).
						compareTo(CommonUtil.DateToDateString(oneEnrollmentSchedule.getScheduleDateBegin())) >= 0
						&& 
						CommonUtil.DateToDateString(new Date()).						
						compareTo(CommonUtil.DateToDateString(oneEnrollmentSchedule.getScheduleDateEnd())) <= 0) {
					oneEnrollmentSchedule.setIsToday(0);
				}else if (CommonUtil.DateToDateString(new Date()).
						compareTo(CommonUtil.DateToDateString(oneEnrollmentSchedule.getScheduleDateBegin())) < 0) {
					oneEnrollmentSchedule.setIsToday(-1);
				}else {
					oneEnrollmentSchedule.setIsToday(1);
				}
			}
		}
		
		return enrollmentSchedules;
	}

	
}
