package com.ichzh.physicalFitness.security;

import com.alibaba.fastjson.JSON;
import com.ichzh.physicalFitness.domain.OperaResult;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 微信小程序认证登录失败处理
 */
@Component
public class WeChatAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        // 响应json
        OperaResult operaResult = new OperaResult();
        operaResult.setMessageType(OperaResult.Error);
        operaResult.setMessageTitle(e.getMessage());
        String json = JSON.toJSONString(operaResult);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(json);
        writer.close();
    }
}
