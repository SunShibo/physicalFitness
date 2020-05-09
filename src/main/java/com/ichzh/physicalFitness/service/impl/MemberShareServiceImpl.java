package com.ichzh.physicalFitness.service.impl;

import com.ichzh.physicalFitness.conf.SelfConfig;
import com.ichzh.physicalFitness.domain.SubscribeMessage;
import com.ichzh.physicalFitness.domain.WeChatToken;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.repository.MemberShareRepository;
import com.ichzh.physicalFitness.service.MemberShareService;
import com.ichzh.physicalFitness.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Service("memberShareService")
public class MemberShareServiceImpl implements MemberShareService {
	
	@Autowired
	private MemberShareRepository memberShareRepository;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "selfConfig")
	private SelfConfig selfConfig;

	/**
	 * 注册会员成为付费会员时，为其分享者奖励学豆.
	 * @param memberId 会员标识号.
	 */
	public void calculatePayMemberGetBeans(String memberId) {
		memberShareRepository.updateMemberSharePay(memberId);
	}

	@Override
	public SubscribeMessage getSubscribeMessage(HttpServletRequest request, String weappKey) {
		Member member = userService.getCurrentLoginUser(request);
		WeChatToken weChatToken = member.getWeChatToken();
		// 获取消息推送数据对象模板
		SubscribeMessage subscribeMessage =selfConfig.getSubscribeMessageMap().get(weappKey);
		subscribeMessage.setTouser(weChatToken.getOpenid());
		return subscribeMessage;
	}

}
