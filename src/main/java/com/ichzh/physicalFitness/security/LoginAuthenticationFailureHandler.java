package com.ichzh.physicalFitness.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.ichzh.physicalFitness.domain.OperaResult;

import net.sf.json.JSONObject;

/**
 * 登录失败处理
 * @author leiyunran
 *
 */
@Component
public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		OperaResult operaResult = new OperaResult();
		operaResult.setMessageTitle(exception.getMessage());
		operaResult.setMessageType(OperaResult.Error);
		response.setHeader("Content-type", "application/json;charset=UTF-8");
	    ServletOutputStream outputStream = response.getOutputStream();
	    outputStream.write(JSONObject.fromObject(operaResult).toString().getBytes("UTF-8"));
	    outputStream.flush();
	    outputStream.close();
	}

}
