package com.ichzh.physicalFitness.service;

import java.util.List;

import com.ichzh.physicalFitness.model.ScheduleReminder;

public interface ScheduleReminderService {

	/**
	 * 保存入学日程提醒设置信息.
	 * @param memberId       会员标识号.
	 * @param serviceBlock   入学阶段.
	 * @param town           区.
	 * @param admissionMode  入学方式.
	 * @return
	 */
	public boolean saveScheduleReminder(String memberId, Integer serviceBlock, Integer town, Integer admissionMode);
	
	
	public void setOtherData(List<ScheduleReminder> scheduleReminders);
	
	/**
	 * 推送日程提醒
	 */
	public void scheduleReminderPush();
	/**
	 * 关注公众号之后，向公众号中推送一条消息.
	 * @param gzhOpenId  公众号的openId
	 */
	public void pushGzhMsgAfterFollow(String gzhOpenId);
	
}
