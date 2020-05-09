package com.ichzh.physicalFitness.run;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.ichzh.physicalFitness.service.ICacheApplicationService;

@Component
@Order(1)
public class CacheApplicationData implements CommandLineRunner {

	@Autowired
	private ICacheApplicationService  cacheApplicationService;
	
	public void run(String... args) throws Exception {
		 cacheApplicationService.cacheData();
	}

}
