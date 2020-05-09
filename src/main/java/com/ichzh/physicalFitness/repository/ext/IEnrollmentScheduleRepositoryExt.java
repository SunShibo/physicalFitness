package com.ichzh.physicalFitness.repository.ext;

import java.util.List;

import com.ichzh.physicalFitness.model.EnrollmentSchedule;

public interface IEnrollmentScheduleRepositoryExt {

	/**
	 * 查询给定日期的日程安排.
	 * @param serviceBlock  学段  10001 幼儿园  10002 小学  10003 初中
	 * @param town  区
	 * @param dates 日期集合.
	 * @return
	 */
	public List<EnrollmentSchedule> queryEnroolmentScheduleBy(Integer serviceBlock, Integer town, List<String> dates);
	
	/**
	 * 根据公众号openId查询该公众号openId对应会员订阅的日程.
	 * @param gzhOpenId       公众号openId
	 * @param schedule_date   日程日期.
	 * @return
	 */
	public List<EnrollmentSchedule> queryEnrollmentScheduleByOpenId(String gzhOpenId, String schedule_date);
}
