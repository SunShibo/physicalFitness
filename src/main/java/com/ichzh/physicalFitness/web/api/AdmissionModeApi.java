package com.ichzh.physicalFitness.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.AdmissionMode;
import com.ichzh.physicalFitness.repository.AdmissionModeRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping({"/admissionmode"})
public class AdmissionModeApi {

	@Autowired
	private AdmissionModeRepository admissionModeRepository;
	
	/**
	 * 查询入学方式
	 * @param serviceBlock    入学条件所属服务模块         
	 * @param town            条件所属区
	 * @return
	 */
	@RequestMapping(value="/queryAdmissionMode", method= {RequestMethod.POST})
	public OperaResult queryAdmModeBy(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town) {
		
		OperaResult result = new OperaResult();
		try
		{
			List<AdmissionMode> admissionModees = admissionModeRepository.queryBy(serviceBlock, town);
			
			result.setResultCode(OperaResult.Success);
			result.setData(admissionModees);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		
		return result;
	}
}
