package com.ichzh.physicalFitness.security;

import com.ichzh.physicalFitness.conf.LoginConfig;
import com.ichzh.physicalFitness.conf.SelfConfig;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.dao.ReflectionSaltSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 自定义登录拦截校验
 * @author audin
 */
@Component
@Slf4j
public class CaptchaDaoAuthenticationProvider extends DaoAuthenticationProvider {

	private final RedisTemplate<String, Object> redisTemplate;;

	private final LoginConfig loginConfig;

	/**
	 * 在本对象创建前，需要先设置好UserDetailsService和PasswordEncoder，所以在构造方法中注入
	 * @param authenticationManagerBuilder
	 * @param userDetailsService
	 * @param passwordEncoder
	 */
	@Autowired
	public CaptchaDaoAuthenticationProvider(AuthenticationManagerBuilder authenticationManagerBuilder, UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, RedisTemplate<String, Object> redisTemplate, LoginConfig loginConfig) {
		super.setUserDetailsService(userDetailsService);
		super.setPasswordEncoder(passwordEncoder);
		ReflectionSaltSource saltSource = new ReflectionSaltSource();
		super.setSaltSource(saltSource);
		// 当前的加密的方式不需要名文的盐
        // saltSource.setUserPropertyToUse("salt");
		authenticationManagerBuilder.authenticationProvider(this);
		this.redisTemplate = redisTemplate;
		this.loginConfig = loginConfig;
	}

	/**
	 * 重写登录认证
	 * @param userDetails
	 * @param token
	 * @throws AuthenticationException
	 */
	protected void additionalAuthenticationChecks(UserDetails userDetails,
												  UsernamePasswordAuthenticationToken token)
			throws AuthenticationException {
		log.info("========================== CaptchaDaoAuthenticationProvider ================================");
		// 获取会员对象
		SecUserDetails secUserDetails = (SecUserDetails) userDetails;
		Member member = secUserDetails.getUser();
		String key = "TOKEN_" + CommonUtil.getUUID();
		member.getWeChatToken().setLoginKey(key);
		// 存入 redis，表示认证登录成功
		redisTemplate.opsForValue().set(key, member, loginConfig.getExpirationMinutes(), TimeUnit.MINUTES);
	}

}
