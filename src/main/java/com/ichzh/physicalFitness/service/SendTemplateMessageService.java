package com.ichzh.physicalFitness.service;

public interface SendTemplateMessageService {

	/**
	 * 发送入学日程通知
	 * @param gzhOpenId 公众号openId
	 * @param first
	 * @param keyword1
	 * @param keyword2
	 * @param sendContent 用于记入发送模板消息日志的内容
	 */
	public void sendEnrollmentScheduleMsg(String gzhOpenId, String first, String keyword1, String keyword2, String sendContent);
	
	/**
	 * 发送获得学豆的通知
	 * @param memberBeans 会员的总学豆数.
	 * @param openId      小程序openId
	 * @param getBeansNum 增加的学豆数.
	 */
    public void pushBeansChange(Integer memberBeans, String openId, Integer getBeansNum);
}
