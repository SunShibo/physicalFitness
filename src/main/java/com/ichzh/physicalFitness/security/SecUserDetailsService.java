package com.ichzh.physicalFitness.security;

import com.ichzh.physicalFitness.conf.SelfConfig;
import com.ichzh.physicalFitness.domain.WeChatToken;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.service.UserService;
import com.ichzh.physicalFitness.util.HttpUtil;

import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @author audin
 */
@Component
@Slf4j
public class SecUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserService userService;

	@Autowired
	private SelfConfig selfConfig;

	/**
	 * 根据微信的一次性授权码，查询微信的基本信息
	 * @param code 微信一次性授权码
	 * @return
	 * @throws UsernameNotFoundException
	 */
	@Override
	public UserDetails loadUserByUsername(String code) throws UsernameNotFoundException {
		log.debug("loadUserByUsername(String) - start");
		if (StringUtils.isEmpty(code)) {
			log.error("小程序登录失败，未获取到授权码");
			throw new BadCredentialsException("小程序登录失败，未获取到授权码");
		}
		// 使用一次性授权码，获取微信的 openId
		Map<String, Object> param = new HashMap<>();
		param.put("appid", selfConfig.getAppid());
		param.put("secret", selfConfig.getSecret());
		param.put("grant_type", selfConfig.getGrantType());
		param.put("js_code", code);
		log.info("获取openId参数：" + JSONObject.fromObject(param));
		String s = HttpUtil.sendGet(selfConfig.getCode2SessionUrl(), param, String.class);
		log.info("小程序登录用户的信息：" + s);
		WeChatToken token = (WeChatToken) JSONObject.toBean(JSONObject.fromObject(s), WeChatToken.class);
		String openid = token.getOpenid();
		if (StringUtils.isEmpty(openid)) {
			log.error("小程序登录失败，" + s);
			throw new BadCredentialsException("小程序登录失败，" + token.getErrmsg());
		}
		// 根据 openId 查询会员信息
		Member member = userService.queryByMemberWeChat(openid);
		// 会员不存在，创建会员对象
		if (member == null) {
			member = userService.buildMember(openid);
		}
		member.setWeChatToken(token);

		// 返回 UserDetails 给 spring security
		UserDetails userDetails = new SecUserDetails(member);
		log.debug("loadUserByUsername(String) - end"); //$NON-NLS-1$
		return userDetails;
	}

}
