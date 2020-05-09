package com.ichzh.physicalFitness.service;

import java.util.List;

import com.ichzh.physicalFitness.model.EnrollmentScheduleList;

public interface EnrollmentScheduleListService {

	/**
	 * 查询给定日期的日程安排_按时间段.
	 * @param serviceBlock  学段  10001 幼儿园  10002 小学  10003 初中
	 * @param town  区
	 * @return
	 */
	public List<EnrollmentScheduleList> queryEnroolmentScheduleListBy(Integer serviceBlock, Integer town, Integer admissionMode);
}
