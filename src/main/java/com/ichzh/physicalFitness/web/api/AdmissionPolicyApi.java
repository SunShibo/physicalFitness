package com.ichzh.physicalFitness.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.AdmissionCondition;
import com.ichzh.physicalFitness.model.AdmissionPolicy;
import com.ichzh.physicalFitness.repository.AdmissionPolicyRepository;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping({"/admissionpolicy"})
public class AdmissionPolicyApi {

	@Autowired
	private AdmissionPolicyRepository admissionPolicyRepository;
	
	/**
	 * 查询入学政策
	 * @param serviceBlock    入学政策所属服务模块         
	 * @param town            条件所属区
	 * @return
	 */
	@RequestMapping(value="/queryAdmissionPolicy", method= {RequestMethod.POST})
	public OperaResult queryAdmPolicyBy(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town) {
		
		OperaResult result = new OperaResult();
		try
		{
			List<AdmissionPolicy> admissionPolicyes = admissionPolicyRepository.queryBy(serviceBlock, town);
			
			result.setResultCode(OperaResult.Success);
			result.setData(admissionPolicyes);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		
		return result;
	}
}
