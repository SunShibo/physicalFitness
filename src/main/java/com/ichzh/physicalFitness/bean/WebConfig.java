package com.ichzh.physicalFitness.bean;

import com.ichzh.physicalFitness.conf.SelfConfig;
import com.ichzh.physicalFitness.interceptor.PaymentInterceptor;
import com.ichzh.physicalFitness.model.SysDict;
import com.ichzh.physicalFitness.service.CommodityLimitNumService;
import com.ichzh.physicalFitness.service.DictService;
import lombok.extern.slf4j.Slf4j;
import net.kaczmarzyk.spring.data.jpa.web.SpecificationArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.List;

/**
 * @author audin
 */
@Configuration
@EnableScheduling
@EnableJpaRepositories
@EnableConfigurationProperties
@Slf4j
public class WebConfig extends WebMvcConfigurerAdapter {

    private final LocaleChangeInterceptor localeChangeInterceptor;

    private final ReloadableResourceBundleMessageSource messageSource;

    private final PaymentInterceptor paymentInterceptor;

    private final DictService dictService;

    private final SelfConfig selfConfig;
    
    private final CommodityLimitNumService commodityLimitNumService;

    @Autowired
    public WebConfig(LocaleChangeInterceptor localeChangeInterceptor, ReloadableResourceBundleMessageSource messageSource, 
    		PaymentInterceptor paymentInterceptor, DictService dictService, SelfConfig selfConfig, CommodityLimitNumService commodityLimitNumService) {
        this.localeChangeInterceptor = localeChangeInterceptor;
        this.messageSource = messageSource;
        this.paymentInterceptor = paymentInterceptor;
        this.dictService = dictService;
        this.selfConfig = selfConfig;
        this.commodityLimitNumService = commodityLimitNumService;
    }

    /*
     * 设置登录地址
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/user/login");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

    /**
     * 添加拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.localeChangeInterceptor);
        // 信息支付校验拦截器
        List<SysDict> list = dictService.querySysDictByDictType(selfConfig.getInfoKindType());
        if (!list.isEmpty()) {
            InterceptorRegistration interceptorRegistration = registry.addInterceptor(this.paymentInterceptor);
            for (SysDict dict : list) {
                interceptorRegistration.addPathPatterns(dict.getDictCode());
                log.info("支付校验拦截器拦截的uri包括：" + dict.getDictCode());
            }
        }
    }

    /*
     * 让Bean验证也使用messages.properties，并支持UTF-8编码
     */
    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(this.messageSource);
        return validator;
    }

    // 支持动态查询
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(new SpecificationArgumentResolver());
    }

}
