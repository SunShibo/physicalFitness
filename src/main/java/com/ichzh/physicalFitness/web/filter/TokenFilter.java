package com.ichzh.physicalFitness.web.filter;

import com.alibaba.fastjson.JSON;
import com.ichzh.physicalFitness.conf.LoginConfig;
import com.ichzh.physicalFitness.conf.SelfConfig;
import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.security.SecUserDetails;
import com.ichzh.physicalFitness.util.ApplicationContextProviderUtil;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * 认证拦截过滤器，自定义判断是否已登录认证，如果已认证，则告诉 spring security 已通过认证
 * 继承 OncePerRequestFilter，确保在一次请求只通过一次filter，而不需要重复执行
 * 过滤器不能使用 @comment 注解，如果使用，那么 com.ichzh.shoppingwas.WebSecurityConfig#configure(org.springframework.security.config.annotation.web.builders.WebSecurity) 就会不生效
 */
@Slf4j
public class TokenFilter extends OncePerRequestFilter {

    private RedisTemplate<String, Object> redisTemplate;

    private LoginConfig loginConfig;

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 从请求头解析出登录标识
        String token = request.getHeader("token");
        log.info("uri=" + request.getRequestURI() + "&token="+token);
        if (StringUtils.isEmpty(token)) {
            handleUnLogin(response);
            return;
        }
        
        // 使用登录标识查询 redis，判断是否已认证登录
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Member member = (Member) valueOperations.get(token);
        // 未登录，响应json
        if (member == null) {
            handleUnLogin(response);
            return;
        }
        // 已登录，告诉 spring security 已通过认证
        UserDetails userDetails = new SecUserDetails(member);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 刷新过期时间
        valueOperations.set(token, member, loginConfig.getExpirationMinutes(), TimeUnit.MINUTES);
        filterChain.doFilter(request, response);
    }

    /**
     * 处理未登录，输出json
     */
    private void handleUnLogin(HttpServletResponse response) throws IOException {
        OperaResult operaResult = new OperaResult();
        operaResult.setMessageType(OperaResult.Error);
        operaResult.setMessageTitle("登录已过期");
        String json = JSON.toJSONString(operaResult);
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        writer.write(json);
        writer.close();
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setLoginConfig(LoginConfig loginConfig) {
        this.loginConfig = loginConfig;
    }


}