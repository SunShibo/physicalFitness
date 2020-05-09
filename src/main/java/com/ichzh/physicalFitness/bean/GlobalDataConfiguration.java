package com.ichzh.physicalFitness.bean;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class GlobalDataConfiguration {
	    @Bean(name="dataSource")  
	    @Primary
	    @ConfigurationProperties(prefix="spring.datasource.first")  
	    public DataSource firstDataSource() {  
	        return DataSourceBuilder.create().build();
	    }  
	    
		@Bean(name = "jdbcTemplate")
	    public JdbcTemplate firstJdbcTemplate(
	            @Qualifier("dataSource") DataSource dataSource) {
	        return new JdbcTemplate(dataSource);
	    }
}
