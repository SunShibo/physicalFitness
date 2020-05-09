package com.ichzh.physicalFitness.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 如果项目中开启了Oauth2认证模块，则需要定义bean: restTemplate
 * OAuth2认证模块已经定义了bean:restTemplate
 */
@Configuration
public class RestTemplateConfig {

	@Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory){
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory(){
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(30000);//单位为ms
        factory.setConnectTimeout(30000);//单位为ms
        return factory;
    }
}
