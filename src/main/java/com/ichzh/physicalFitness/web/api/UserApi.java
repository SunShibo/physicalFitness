/**
 * 
 */
package com.ichzh.physicalFitness.web.api;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.repository.MemberRepository;
import com.ichzh.physicalFitness.security.SecUserDetails;
import com.ichzh.physicalFitness.service.ICacheApplicationService;
import com.ichzh.physicalFitness.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping({"/api","/ajax"})
public class UserApi {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private ICacheApplicationService cacheApplicationService;

	@RequestMapping(method = RequestMethod.GET, value = "/me")
	public Member info() {
		log.debug("info() - start"); //$NON-NLS-1$
		Member user = userService.getLoginUser();
		log.debug("info() - end, user={}", user); //$NON-NLS-1$
		return user;
	}
	
	/**
	 * 查询已登录用户信息.
	 * @return
	 */
	@RequestMapping(value="/user/query_logined_user", method=RequestMethod.GET)
	public Member queryLoginedUser(HttpServletRequest request){
		Member ret = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof SecUserDetails) {
			ret = ((SecUserDetails) principal).getUser();
			request.getSession().setAttribute("currentLoginUser", ret);
		}
		Member member = new Member();
		if (ret != null) {
			// 敏感信息不外露
			BeanUtils.copyProperties(ret, member);
			member.setMemberId(null);
			member.setMemberPassword(null);
			member.setMemberUsername(null);
		}
		return member;
	}

	@RequestMapping(value = "/userinfo/query_user_info_by_uuid", method = { RequestMethod.POST,RequestMethod.GET })
	public Member queryUserInfoByUuid(String uuid ) {
		return memberRepository.findOne(uuid);
	}

}
