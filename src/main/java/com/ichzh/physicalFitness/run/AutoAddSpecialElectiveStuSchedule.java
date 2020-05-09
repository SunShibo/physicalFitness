package com.ichzh.physicalFitness.run;

import java.text.SimpleDateFormat;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AutoAddSpecialElectiveStuSchedule {
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//	@Value("${server.taskrun}")
//	private int taskrun;
//	
//	@Autowired
//	private SpecialElectiveStuRepository specialElectiveStuRepository;
//	/**
//	 * 自动同步补课次数（补课表如果有某学生数据就不处理该学生）
//	 */
//	@Scheduled(cron = "0 0 6 * * *")//每半个小时执行一次
//	public void addSpecialElectiveStu(){
//		if(taskrun==1){
//			specialElectiveStuRepository.autoAddSpecialElectiveStu();
//		}
//	}
}
