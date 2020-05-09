package com.ichzh.physicalFitness;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 
 * @author audin
 *
 */
@SpringBootApplication
@EnableJpaRepositories("com.ichzh.physicalFitness.repository") // 扫描 repository
@Slf4j
public class PhysicalFitnessApplication extends SpringBootServletInitializer {
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		log.debug("configure(SpringApplicationBuilder) - start"); //$NON-NLS-1$

		SpringApplicationBuilder sab = application.sources(PhysicalFitnessApplication.class);
		
		log.debug("configure(SpringApplicationBuilder) - end"); //$NON-NLS-1$
        return sab;
    }

	public static void main(String[] args) throws Exception {
		log.debug("main(String[]) - start"); //$NON-NLS-1$

		SpringApplication.run(PhysicalFitnessApplication.class, args);
		
		log.debug("main(String[]) - end"); //$NON-NLS-1$
	}

//	@Bean
//	public EmbeddedServletContainerCustomizer containerCustomizer(){
//	       return new EmbeddedServletContainerCustomizer() {
//	           @Override
//	           public void customize(ConfigurableEmbeddedServletContainer container) {
//	                container.setSessionTimeout(20);//单位为S
//	          }
//	    };
//	}
	
	@Bean
	protected ServletContextListener listener() {
		return new ServletContextListener() {
			@Override
			public void contextInitialized(ServletContextEvent sce) {
				log.info("ServletContext initialized");
			}

			@Override
			public void contextDestroyed(ServletContextEvent sce) {
				log.info("ServletContext destroyed");
			}
		};
	}
}
