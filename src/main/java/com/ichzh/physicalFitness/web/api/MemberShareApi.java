package com.ichzh.physicalFitness.web.api;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.repository.MemberShareRepository;
import com.ichzh.physicalFitness.service.UserService;

@RestController
@RequestMapping({"/memberShare"})
public class MemberShareApi {
	
	@Autowired
	private UserService userService;

	@Autowired
	private MemberShareRepository memberShareRepository;
	
	@RequestMapping(value="/updateMemberShare", method=RequestMethod.POST)
	public OperaResult updateMemberShare(HttpServletRequest request, String shareUserId) {
		
		OperaResult result = new OperaResult();
		if(StringUtils.isEmpty(shareUserId)) {
			result.setResultCode(OperaResult.Error);
			return result;
		}
		
		Member member = userService.getCurrentLoginUser(request);
		if(shareUserId.equals(member.getMemberId())) {
			result.setResultCode(OperaResult.Error);
			return result;
		}
			
		memberShareRepository.updateMemberShare(member.getMemberId(), shareUserId);
		result.setResultCode(OperaResult.Success);
		return result;
	}
}
