package com.ichzh.physicalFitness.web.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ichzh.physicalFitness.conf.SelfConfig;
import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.repository.MemberRepository;
import com.ichzh.physicalFitness.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping({"/member"})
public class MemberApi {

	@Autowired
	UserService userService;
	
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	SelfConfig selfConfig;
	
	/**
	 * 检查当前登录用户是否为付费会员
	 * @return
	 */
	@RequestMapping(value="/checkIfPayMember", method= {RequestMethod.GET, RequestMethod.POST})
	public OperaResult checkIfPayMember(HttpServletRequest request) {
		
		OperaResult result = new OperaResult();
		HashMap<String, String> resultData = new HashMap<String, String>();
		//获得当前登录用户
		Member currentLoginUser = userService.queryNewestMember(request);
		if (currentLoginUser == null) {
			result.setResultCode(OperaResult.Success);
			//2: 当前用户的登录状态已失效
			resultData.put("is_pay_member", "2");
			result.setData(resultData);
		}else {
			//会员等级
			Integer memberGrade = currentLoginUser.getMemberGrade();
			if (memberGrade == null) {
				result.setResultCode(OperaResult.Success);
				//3: 当前用户的会员等级异常
				resultData.put("is_pay_member", "3");
				result.setData(resultData);
			}else if(memberGrade.intValue() == 2){
				result.setResultCode(OperaResult.Success);
				//1: 当前用户是普通付费会员
				resultData.put("is_pay_member", "1");
				result.setData(resultData);
			}else if (memberGrade.intValue() == 1) {
				result.setResultCode(OperaResult.Success);
				//0: 当前用户是普通注册会员
				resultData.put("is_pay_member", "0");
				result.setData(resultData);
			}
		}
		
		return result;
	}
	
	/**
	 * 检查当前登录用户是否已经绑定过手机号
	 * @return
	 */
	@RequestMapping(value="/checkIfBindPhone", method= {RequestMethod.POST})
	public OperaResult checkIfBindPhone(HttpServletRequest request) {
		OperaResult result = new OperaResult();
		HashMap<String, String> resultData = new HashMap<String, String>();
		//获得当前登录用户ID
		String memberId = userService.getCurrentLoginUserId(request);
		
		if (memberId == null) {
			result.setResultCode(OperaResult.Success);
			//2: 当前用户的登录状态已失效
			resultData.put("is_bind_phone", "2");
			result.setData(resultData);
		}else {
			
			Member member = memberRepository.findByMemberId(memberId);
			if (member == null) {
				result.setResultCode(OperaResult.Success);
				//3: 当前用户的会员信息异常
				resultData.put("is_bind_phone", "3");
				result.setData(resultData);
			}else if (StringUtils.isNotEmpty(member.getMemberMobile())) {
				result.setResultCode(OperaResult.Success);
				//1: 当前用户已绑定手机号
				resultData.put("is_bind_phone", "1");
				resultData.put("userMobile", member.getMemberMobile());
				result.setData(resultData);
			}else{
				result.setResultCode(OperaResult.Success);
				//0: 当前用户未绑定手机号
				resultData.put("is_bind_phone", "0");
				result.setData(resultData);
			}
		}
		
		return result;
	}
	
	
	/**
	 * 获取小程序 我的 栏目需要的数据
	 * @return
	 */
	@RequestMapping(value="/getMyProfile", method= {RequestMethod.POST})
	public OperaResult getMyProfile(HttpServletRequest request) {
		OperaResult result = new OperaResult();
		//获得当前登录用户ID
		String memberId = userService.getCurrentLoginUserId(request);
		
		if (memberId == null) {
			result.setResultCode(OperaResult.Error);
		}else {
			
			Member member = memberRepository.findByMemberId(memberId);
			if (member == null) {
				result.setResultCode(OperaResult.Error);
			}else{
				result.setResultCode(OperaResult.Success);
				result.setData(userService.wrapMyProfileInfo(member));
			}
		}
		
		return result;
	}
	
	/**
	 * 用户订阅服务消息时，指定的模板ID
	 * @param tplKind  1：日程提醒  2：学豆变更
	 * @return
	 */
	@RequestMapping(value="/getServiceMsgTemplateId", method= {RequestMethod.POST})
	public List<String> getServiceMsgTemplateId(@RequestParam(value = "tplKind", required = true) Integer tplKind){
		List<String> ret = new ArrayList<String>();
		if (tplKind.intValue() == 1) {
			String  tplId = selfConfig.getSubscribeMessageMap().get("scheduleReminder").getTemplate_id();
			ret.add(tplId);
		}
		return ret;
	}
}
