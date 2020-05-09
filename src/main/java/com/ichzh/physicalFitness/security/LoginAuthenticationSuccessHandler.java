package com.ichzh.physicalFitness.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.service.UserService;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

/**
 * 登录成功后的处理
 * 比如：日志记录登录成功
 * @author leiyunran
 *
 */
@Component
@Slf4j
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final UserService userService;

	@Autowired
	public LoginAuthenticationSuccessHandler (UserService userService) {
		this.userService = userService;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// 登录流程走到此处，还未设置session，所以还不能使用com.ichzh.tpl_sbamysql.service.UserService.getCurrentLoginUser(HttpServletRequest)方法
		// 获取用户只能使用com.ichzh.tpl_sbamysql.service.UserService.getLoginUser()
		Member member = userService.getLoginUser();
		log.info(member + " 登录成功");
		OperaResult operaResult = new OperaResult();
		operaResult.setMessageTitle("登录成功");
		operaResult.setMessageType(OperaResult.Success);
		response.setHeader("Content-type", "application/json;charset=UTF-8");
	    ServletOutputStream outputStream = response.getOutputStream();
	    outputStream.write(JSONObject.fromObject(operaResult).toString().getBytes("UTF-8"));
	    outputStream.flush();
	    outputStream.close();
		// 重定向到指定页面
		//response.sendRedirect(request.getContextPath() + "/index_page");
	}

}
