package com.ichzh.physicalFitness.security;

import com.ichzh.physicalFitness.conf.LoginConfig;
import com.ichzh.physicalFitness.util.CommonUtil;
import com.ichzh.physicalFitness.web.filter.TokenFilter;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.*;
import java.io.IOException;

//@EnableOAuth2Sso
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final WeChatAuthenticationSuccessHandler loginAuthenticationSuccessHandler;

    private final WeChatAuthenticationFailureHandler loginAuthenticationFailureHandler;

    private final HttpFirewall httpFirewall;

    private final LoginConfig loginConfig;

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public WebSecurityConfig(WeChatAuthenticationSuccessHandler loginAuthenticationSuccessHandler, WeChatAuthenticationFailureHandler loginAuthenticationFailureHandler, HttpFirewall httpFirewall, LoginConfig loginConfig, RedisTemplate<String, Object> redisTemplate) {
        this.loginAuthenticationSuccessHandler = loginAuthenticationSuccessHandler;
        this.loginAuthenticationFailureHandler = loginAuthenticationFailureHandler;
        this.httpFirewall = httpFirewall;
        this.loginConfig = loginConfig;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void configure(WebSecurity web) {
        // 不需要登录也能访问的url规则，在此设置
        web.ignoring().antMatchers("/", "/captcha**", "/uploads/**", "/webjars/**",
                "/demo/**", "/register", "/register/**", "/css/**", "/images/**",
                "/images/icon/**", "/js/**", "/test/js/**", "/callBack/**", "/business_images/**",
                "/notwxchart/**", "/wechatclientservice/**", "/schoollocation/**");
        web.httpFirewall(this.httpFirewall);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().sameOrigin();
        // 禁用csrf配置
        http.csrf().disable();
        // 关闭session管理，使用token机制处理
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 创建过滤器对象，在过滤器中判断请求是否已认证。
        TokenFilter tokenFilter = new TokenFilter();
        tokenFilter.setLoginConfig(this.loginConfig);
        tokenFilter.setRedisTemplate(this.redisTemplate);
        // 自定义登录拦截。使用 addFilterAfter，post登录请求不会经过过滤器。
        http.addFilterAfter(tokenFilter, UsernamePasswordAuthenticationFilter.class).authorizeRequests();
        FormLoginConfigurer<HttpSecurity> httpSecurityFormLoginConfigurer = http.formLogin();
        // post 请求时才会进入 UserDetailsService
        httpSecurityFormLoginConfigurer.loginPage("/user/login");
        // 登录失败处理
        httpSecurityFormLoginConfigurer.failureHandler(loginAuthenticationFailureHandler);
        // 登录成功处理
        httpSecurityFormLoginConfigurer.successHandler(loginAuthenticationSuccessHandler);
        // 接收参数
        httpSecurityFormLoginConfigurer.usernameParameter("code");
        httpSecurityFormLoginConfigurer.permitAll();
    }

    private Filter logFilter() {
        return new GenericFilterBean() {
            @Override
            public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                    throws IOException, ServletException {
                // 生成一个新的loggger_id
                String loggerId = CommonUtil.getUUID();
                MDC.put("logger_id", loggerId);

                try {
                    chain.doFilter(request, response);
                } finally {
                    // 请求结束后要把logger_id移除
                    MDC.remove("logger_id");
                }
            }
        };
    }

}
