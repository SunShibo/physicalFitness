package com.ichzh.physicalFitness.web.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.EnrollmentSchedule;
import com.ichzh.physicalFitness.model.EnrollmentScheduleList;
import com.ichzh.physicalFitness.service.EnrollmentScheduleListService;
import com.ichzh.physicalFitness.service.EnrollmentScheduleService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping({"/enrollmentSchedule"})
public class EnrollmentScheduleApi {

	@Autowired
	EnrollmentScheduleService enrollmentScheduleService;
	@Autowired
	EnrollmentScheduleListService enrollmentScheduleListService;
	
	/**
	 * 查询第几周的入学日程
	 * @param serviceBlock  学段：10001 幼儿园  10002 小学  10003 初中
	 * @param town   区
	 * @param weekNum  0: 表示查询当前周的日程；1：表示查询下一周的日程；-1：表示查询上一周的日程
	 * @return
	 */
	@RequestMapping(value="/getEnrollmentScheduleOfSomeoneWeek", method= {RequestMethod.POST})
	public OperaResult getEnrollmentScheduleOfSomeoneWeek(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town,
			@RequestParam(value = "weekNum", required = true) Integer weekNum,
			@RequestParam(value = "admissionMode", required = true) Integer admissionMode) {
		
		OperaResult ret = new OperaResult();
		try
		{
			List<EnrollmentSchedule> enrollmentSchedules = enrollmentScheduleService.queryEnroolmentScheduleBy(
					serviceBlock, town, weekNum, admissionMode);
			
			ret.setResultCode(OperaResult.Success);
			ret.setData(enrollmentSchedules);
		}catch(Exception ex) {
			ret.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		  
		return ret;
	}
	
	/**
	 * 查询第几周的入学日程
	 * @param serviceBlock  学段：10001 幼儿园  10002 小学  10003 初中
	 * @param town   区
	 * @return
	 */
	@RequestMapping(value="/getEnrollmentSchedule", method= {RequestMethod.POST})
	public OperaResult getEnrollmentSchedule(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town,
			@RequestParam(value = "admissionMode", required = true) Integer admissionMode) {
		
		OperaResult ret = new OperaResult();
		try
		{
			List<EnrollmentSchedule> enrollmentSchedules = enrollmentScheduleService.queryEnroolmentScheduleBy(serviceBlock, 
					town, admissionMode);
			ret.setResultCode(OperaResult.Success);
			ret.setData(enrollmentSchedules);
		}catch(Exception ex) {
			ret.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		  
		return ret;
	}
	
	@RequestMapping(value="/getEnrollmentScheduleList", method= {RequestMethod.POST})
	public OperaResult getEnrollmentScheduleList(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town,
			@RequestParam(value = "admissionMode", required = true) Integer admissionMode) {
		
		OperaResult ret = new OperaResult();
		try
		{
			List<EnrollmentScheduleList> enrollmentSchedules = enrollmentScheduleListService.queryEnroolmentScheduleListBy(serviceBlock, 
					town, admissionMode);
			ret.setResultCode(OperaResult.Success);
			ret.setData(enrollmentSchedules);
		}catch(Exception ex) {
			ret.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		  
		return ret;
	}
	
	@RequestMapping(value="/getEnrollmentScheduleAdmissionMode", method= {RequestMethod.POST})
	public OperaResult getEnrollmentScheduleAdmissionMode(
			@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town) {
		
		OperaResult ret = new OperaResult();
		try{
			List<Map<String, Object>> lstDictInfo = enrollmentScheduleService.queryEnrollmentScheduleAdmissionMode(
					serviceBlock, town);
			
			ret.setResultCode(OperaResult.Success);
			ret.setData(lstDictInfo);
		}catch(Exception ex) {
			ret.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		return ret;
	}
	
	
	
}
