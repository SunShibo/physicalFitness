package com.ichzh.physicalFitness.security;

import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

//@Configuration
//@EnableResourceServer
public class OAuthResourceConfig extends ResourceServerConfigurerAdapter {
//	@Autowired
//	private TokenStore tokenStore;
//
//	@Override
//	public void configure(ResourceServerSecurityConfigurer resources)
//			throws Exception {
//		// TokenStore tokenStore = new JdbcTokenStore(dataSource);
//		resources.resourceId("api").tokenStore(tokenStore);
//	}
//
//	@Override
//	public void configure(HttpSecurity http) throws Exception {
//		//@formatter:off
//    	http
//    		.requestMatchers().antMatchers("/api/**")
//    		.and()
//    		.authorizeRequests()
//    		
//    		// 获取当前用户的基本信息
//    		.antMatchers(HttpMethod.GET,"/api/me").access("#oauth2.hasScope('user_info_get')")
//    		// 更新当前用户的基本信息（部分属性更新）
//			.antMatchers(HttpMethod.PATCH, "/api/me").access("#oauth2.hasScope('user_info_update')")
//    		// 更新当前用户的基本信息（全部属性更新）
//			.antMatchers(HttpMethod.POST, "/api/me").access("#oauth2.hasScope('user_info_update')")
//			// 更新当前用户的密码
//			.antMatchers(HttpMethod.POST,"/api/me/pwd").access("#oauth2.hasScope('user_pwd_update')")
//			.antMatchers(HttpMethod.GET, "/api/user/**").access("#oauth2.hasScope('user_info_get')");
//    	//@formatter:on
//	}
}
