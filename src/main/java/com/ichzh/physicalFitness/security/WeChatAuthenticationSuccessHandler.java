package com.ichzh.physicalFitness.security;

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.run.TimeTask;
import com.ichzh.physicalFitness.service.ResponseService;
import com.ichzh.physicalFitness.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录成功后的处理
 */
@Component
@Slf4j
public class WeChatAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private ResponseService responseService;

    @Resource(name = "timeTask")
    private TimeTask timeTask;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("============================ 登录成功后，更新微信的用户基本信息 ====================================");
        // 微信昵称
        String nickName = request.getParameter("nickName");
        // 性别（微信中性别 0：未知、1：男、2：女）
        Integer gender = null;
        String parameter = request.getParameter("gender");
        if (StringUtils.isNotEmpty(parameter)) {
            gender = Integer.valueOf(parameter);
        }
        // 微信头像地址
        String avatarUrl = request.getParameter("avatarUrl");
        // 用户绑定的手机号（国外手机号会有区号）
        String phoneNumber = request.getParameter("phoneNumber");
        //小程序客户端返回的加密数据
        String encryptedData = request.getParameter("encryptedData");
        String iv = request.getParameter("iv");
        
        
        // spring security 已经认证过，已从微信接口服务获取到微信的 openId，在登录对象中
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SecUserDetails secUserDetails = (SecUserDetails) principal;
        Member member = secUserDetails.getUser();
        String key = member.getWeChatToken().getLoginKey();
        
        String session_key = member.getWeChatToken().getSession_key();
        log.info("token="+key);

        // 异步保存统一账户信息
        timeTask.saveUnifiedAccount(member.getWeChatToken());
        // 判断是否需要更新用户信息
        boolean updateUser = userService.isUpdateUser(member, nickName, gender, avatarUrl, phoneNumber);
        OperaResult operaResult = new OperaResult();
        operaResult.setMessageType(OperaResult.Success);
        operaResult.setMessageTitle("小程序登录成功");
        operaResult.setData(key);
        // 有会员ID，且不需要更新，则直接响应登录成功
        if (StringUtils.isNotEmpty(member.getMemberId()) && !updateUser) {
            responseService.write(response, operaResult);
            return;
        }
        // 如果用户为首次登录，执行插入操作;非首次登录，执行更新操作
        userService.modifyUser(member, key);
        responseService.write(response, operaResult);
    }


}
