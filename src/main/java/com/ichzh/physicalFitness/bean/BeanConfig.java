package com.ichzh.physicalFitness.bean;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;
import com.ichzh.physicalFitness.conf.HttpsConfig;
import com.ichzh.physicalFitness.conf.LoginConfig;
import com.ichzh.physicalFitness.security.CaptchaAuthenticationDetailsSource;
import com.ichzh.physicalFitness.util.YamlResourceLoadUtil;
import com.yodoo.spring.dynamicquery.SqlMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.deploy.SecurityCollection;
import org.apache.catalina.deploy.SecurityConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.extras.conditionalcomments.dialect.ConditionalCommentsDialect;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Configuration
@Slf4j
public class BeanConfig {

    private final LoginConfig loginConfig;

    private final HttpsConfig httpsConfig;

    @Autowired
    public BeanConfig (LoginConfig loginConfig, HttpsConfig httpsConfig) {
        this.loginConfig = loginConfig;
        this.httpsConfig = httpsConfig;
    }

    /**
     * 这个加密方式不需要名文的盐，所以数据库中的salt字段已用不到
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        // 此处开启不让检测‘/’符号
        firewall.setAllowUrlEncodedSlash(true);
        firewall.setAllowBackSlash(true);
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }

    @Bean
    public CaptchaAuthenticationDetailsSource captchaAuthenticationDetailsSource() {
        return new CaptchaAuthenticationDetailsSource();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public Config captchaConfig() {
        Properties properties = new Properties();
        InputStream is = getClass().getClassLoader().getResourceAsStream("captcha.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
        }
        return new Config(properties);
    }

    @Bean
    public Producer captchaProducer(Config captchaConfig) {
        return captchaConfig.getProducerImpl();
    }

    /*
     * 让thymeleaf支持解析注释中的内容
     */
    @Bean
    public ConditionalCommentsDialect conditionalCommentDialect() {
        return new ConditionalCommentsDialect();
    }

    /*
     * 设置通过URL参数改变语言环境
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.CHINA);
        return slr;
    }

    /*
     * 设置资源文件路径和字符集
     */
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:/locale/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(3600); // refresh cache once per hour
        return messageSource;
    }

    /**
     * 加载sql语句到内存
     * @return
     * @throws IOException
     */
    @Bean
    public SqlMap sqlMap() throws IOException {
        String[] sqlYmls = loginConfig.getSqlYmls();
        log.info("加载的sql配置文件：" + Arrays.toString(sqlYmls));
        Map<String, String> combineResultMap = new HashMap<String, String>();
        for (String sqlYml : sqlYmls) {
            List<Map<String, String>> list = YamlResourceLoadUtil.loadYamlResource(sqlYml);
            combineResultMap.putAll(list.get(0));
        }

        SqlMap sqlMap = new SqlMap();
        sqlMap.setSqls(combineResultMap);
        return sqlMap;
    }

    /*
     * 使用@JsonView时，让未标记的属性也输出
     * 日期格式化输出
     */
    @Bean
    public Jackson2ObjectMapperBuilder jacksonBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.indentOutput(true);
        builder.dateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        builder.defaultViewInclusion(true);
        return builder;
    }

    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer(){
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/404");
                ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500");
                container.addErrorPages(error404Page,error500Page);
            }

        };
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {

        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        // 根据配置来决定是否开启 https 协议
        if (httpsConfig.isEnable()) {
            tomcat.addAdditionalTomcatConnectors(httpsConfig.createSslConnector()); // 添加http
        }
        tomcat.addContextCustomizers(new TomcatContextCustomizer() {

            public void customize(Context context) {
                SecurityConstraint constraint = new SecurityConstraint();
                SecurityCollection collection = new SecurityCollection();

                //http方法
                collection.addMethod("PUT");
                collection.addMethod("DELETE");
                collection.addMethod("HEAD");
                collection.addMethod("OPTIONS");
                collection.addMethod("TRACE");

                //url匹配表达式
                collection.addPattern("/*");
                constraint.addCollection(collection);
                constraint.setAuthConstraint(true);
                context.addConstraint(constraint );

                //设置使用httpOnly
                context.setUseHttpOnly(true);
            }
        });
        return tomcat;
    }

}
