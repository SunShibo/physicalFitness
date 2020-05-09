package com.ichzh.physicalFitness.web.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.AdmissionTime;
import com.ichzh.physicalFitness.repository.AdmissionTimeRepository;
import com.ichzh.physicalFitness.service.AdmissionTimeService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping({"/admissiontime"})
public class AdmissionTimeApi {

	@Autowired
	private AdmissionTimeRepository admissionTimeRepository;
	
	@Autowired
	private AdmissionTimeService admissionTimeService;
	
	/**
	 * 查询入学时间
	 * @param serviceBlock    入学时间所属服务模块         
	 * @param town            条件所属区
	 * @return
	 */
	@RequestMapping(value="/queryAdmTime", method= {RequestMethod.POST})
	public OperaResult queryAdmTimeBy(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town) {
		
		OperaResult result = new OperaResult();
		try
		{
			List<AdmissionTime> admissionTimees = admissionTimeRepository.queryBy(serviceBlock, town);
			//按时间分组进行分组
			Map<String, List<AdmissionTime>> admTimesAfterGroup = admissionTimeService.groupAdmissionTime(admissionTimees);
			
			result.setResultCode(OperaResult.Success);
			result.setData(admTimesAfterGroup);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		
		return result;
	}
}
