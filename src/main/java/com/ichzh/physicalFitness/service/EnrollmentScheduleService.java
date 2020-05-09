package com.ichzh.physicalFitness.service;

import java.util.List;
import java.util.Map;

import com.ichzh.physicalFitness.model.EnrollmentSchedule;

public interface EnrollmentScheduleService {

	/**
	 * 查询给定日期的日程安排.
	 * @param serviceBlock  学段  10001 幼儿园  10002 小学  10003 初中
	 * @param town  区
	 * @param weekNum 0: 表示查询当前周的日程；1：表示查询下一周的日程；-1：表示查询上一周的日程.
	 * @return
	 */
	public List<EnrollmentSchedule> queryEnroolmentScheduleBy(Integer serviceBlock, Integer town, Integer weekNum, 
			Integer admissionMode);
	
	/**
	 * 查询给定日期的日程安排.
	 * @param serviceBlock  学段  10001 幼儿园  10002 小学  10003 初中
	 * @param town  区
	 * @return
	 */
	public List<EnrollmentSchedule> queryEnroolmentScheduleBy(Integer serviceBlock, Integer town, Integer admissionMode);
	
	public List<Map<String, Object>> queryEnrollmentScheduleAdmissionMode(Integer serviceBlock, Integer town);

}
