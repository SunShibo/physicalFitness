package com.ichzh.physicalFitness.web.api;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;
import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.repository.MemberRepository;
import com.ichzh.physicalFitness.repository.MemberShareRepository;
import com.ichzh.physicalFitness.service.UserService;
import com.ichzh.physicalFitness.util.AES_Encrypt;

import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping({"/userMobile"})
@Slf4j
public class WechatUserMobileApi {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private MemberShareRepository memberShareRepository;
	

	@RequestMapping(value="/getUserMobile", method=RequestMethod.POST)
	public OperaResult getUserMobile(HttpServletRequest request, String encryptedData, String iv) {
		
		OperaResult result = new OperaResult();
		if(StringUtils.isEmpty(encryptedData) || StringUtils.isEmpty(iv)) {
			result.setResultCode(OperaResult.Error);
			return result;
		}
		
		Member member = userService.queryNewestMember(request);
		String userMobile = AES_Encrypt.AESDecrypt(member.getWeChatToken().getSession_key(), encryptedData, iv);
		
		if(StringUtils.isEmpty(userMobile)) {
			result.setResultCode(OperaResult.Error);
			return result;
		}
		
		log.info("================userMobile" + userMobile + "===============");
		Map<String, Object> mobileMap = JSON.parseObject(userMobile); 
		String phoneNumber = mobileMap.get("phoneNumber").toString();
		
		// 修改手机号码
		memberRepository.updateMemberPhone(member.getMemberId(), phoneNumber);
		// 更新学豆信息
		memberShareRepository.updateMemberShareBangdingPhoneNumber(member);
		
		result.setResultCode(OperaResult.Success);
		result.setData(phoneNumber);
		return result;
    }
	
	
	@RequestMapping(value="/getLoginUserInfo", method=RequestMethod.POST)
	public OperaResult getLoginUserInfo(HttpServletRequest request) {
		OperaResult result = new OperaResult();
		Member member = userService.getCurrentLoginUser(request);
		result.setResultCode(OperaResult.Success);
		result.setData(member.getMemberId());
		return result;
    } 
}
