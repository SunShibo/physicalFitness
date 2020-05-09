package com.ichzh.physicalFitness.service;

import javax.servlet.http.HttpServletRequest;

import com.ichzh.physicalFitness.domain.SubscribeMessage;

public interface MemberShareService {

	/**
	 * 注册会员成为付费会员时，为其分享者奖励学豆.
	 * @param memberId 会员标识号.
	 */
	public void calculatePayMemberGetBeans(String memberId);

	/**
	 * 发送订阅消息需要的参数对象
	 * （不包括accessToken）
	 *
	 * @param request
	 * @return
	 */
	SubscribeMessage getSubscribeMessage(HttpServletRequest request, String weappKey);
}
