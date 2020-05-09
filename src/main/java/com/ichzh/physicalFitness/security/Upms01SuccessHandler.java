package com.ichzh.physicalFitness.security;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.ichzh.physicalFitness.model.Member;

@Slf4j
public class Upms01SuccessHandler implements LogoutSuccessHandler {

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	@Override
	public void onLogoutSuccess(HttpServletRequest request,
								HttpServletResponse response, Authentication authentication) throws IOException {
		if(authentication !=null){
			SecUserDetails sud = (SecUserDetails) authentication.getPrincipal();
			Member loginUser = sud.getUser();
		}
		handle(request, response, authentication);
	}

	protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
//		String targetUrl = determineTargetUrl(authentication);
		redirectStrategy.sendRedirect(request, response, "http://kfsj.bjedu.cn/Home/Public/logout");
	}

	protected String determineTargetUrl(Authentication authentication) {
		boolean isUser = false;
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		for (GrantedAuthority grantedAuthority : authorities) {
			if (grantedAuthority.getAuthority().equals("ROLE_USER")) {
				isUser = true;
				break;
			}
		}
		if (isUser) {
			return "/index.jsp";
		} else {
			throw new IllegalStateException();
		}
	}


}
