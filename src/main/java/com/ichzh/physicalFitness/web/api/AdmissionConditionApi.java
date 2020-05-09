package com.ichzh.physicalFitness.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.dto.AdmissionConditonDto;
import com.ichzh.physicalFitness.dto.StudentStatusDto;
import com.ichzh.physicalFitness.model.AdmissionCondition;
import com.ichzh.physicalFitness.model.ConditionLabor;
import com.ichzh.physicalFitness.model.ConditionResidencePermit;
import com.ichzh.physicalFitness.repository.AdmissionConditionRepository;
import com.ichzh.physicalFitness.service.AdmissionConditionService;
import com.ichzh.physicalFitness.service.ICacheApplicationService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping({"/admissioncondition"})
public class AdmissionConditionApi {

	@Autowired
	private AdmissionConditionRepository admissionConditionRepository;
	@Autowired
	private AdmissionConditionService admissionConditionService;
	@Autowired
	private ICacheApplicationService cacheApplicationService;
	/**
	 * 查询入学条件
	 * @param serviceBlock    入学条件所属服务模块         
	 * @param town            条件所属区
	 * @param studentStatus   标签_学籍
	 * @param householdRegistration  标签_户籍
	 * @param residence 标签_居住
	 * @return
	 */
	@RequestMapping(value="/queryAdmissionCondition", method= {RequestMethod.POST})
	public OperaResult queryAdmConBy(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town, 
			@RequestParam(value = "studentStatus", required = false) Integer studentStatus,
			@RequestParam(value = "householdRegistration", required = false) Integer householdRegistration,
			@RequestParam(value = "residence", required = false) Integer residence) {
		
		OperaResult result = new OperaResult();
		try
		{
			
			List<AdmissionCondition> admissionCones = admissionConditionRepository.queryBy(serviceBlock, town, studentStatus, householdRegistration, residence);
			AdmissionConditonDto calAdmissionResultAndPropose = admissionConditionService.calAdmissionResultAndPropose(admissionCones);
			result.setResultCode(OperaResult.Success);
			result.setData(calAdmissionResultAndPropose);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		
		return result;
	}
	
	/**
	 * 查询搜索条件中的学籍筛选数据
	 * @param serviceBlock 服务模块  
	 * @param town   所属区
	 * @return
	 */
	@RequestMapping(value="/queryStudentStatus", method= {RequestMethod.POST})
	public OperaResult queryStudentStatus(
			@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town) {
		OperaResult result = new OperaResult();
		try
		{
			List<StudentStatusDto> studentStatuses = admissionConditionRepository.queryStudentStatus(serviceBlock, town);
			
			result.setResultCode(OperaResult.Success);
			result.setData(studentStatuses);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		return result;
	}
	
	
	/**
	 * 查询搜索条件中的户籍筛选数据
	 * @param serviceBlock   入学条件所属服务模块
	 * @param town           条件所属区
	 * @param studentStatus  标签_学籍
	 * @return
	 */
	@RequestMapping(value="/queryHouseholdRegistration", method= {RequestMethod.POST})
	public OperaResult queryHouseholdRegistration(
			@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town,
			@RequestParam(value = "studentStatus", required = false) Integer studentStatus) {
		OperaResult result = new OperaResult();
		try
		{
			List<StudentStatusDto> studentStatuses = admissionConditionRepository.queryHouseholdRegistration(serviceBlock, town, studentStatus);
			
			result.setResultCode(OperaResult.Success);
			result.setData(studentStatuses);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		return result;
	}
	
	
	/**
	 * 查询搜索条件中的户籍筛选数据
	 * @param serviceBlock   入学条件所属服务模块
	 * @param town           条件所属区
	 * @param studentStatus  标签_学籍
	 * @return
	 */
	@RequestMapping(value="/queryResidence", method= {RequestMethod.POST})
	public OperaResult queryResidence(
			@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town,
			@RequestParam(value = "studentStatus", required = false) Integer studentStatus,
			@RequestParam(value = "householdRegistration", required = false) Integer householdRegistration) {
		OperaResult result = new OperaResult();
		try
		{
			List<StudentStatusDto> studentStatuses = admissionConditionRepository.queryResidence(serviceBlock, town, 
					studentStatus, householdRegistration);
			result.setResultCode(OperaResult.Success);
			result.setData(studentStatuses);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		return result;
	}
	
	
	/**
	 * 查询入学条件_务工条件或结果
	 * @param serviceBlock    入学条件所属服务模块         
	 * @param town            条件所属区
	 * @param householdRegistration  标签_户籍
	 * @param residence 标签_居住
	 * @return
	 */
	@RequestMapping(value="/queryAdmissionConditionLabor", method= {RequestMethod.POST})
	public OperaResult queryAdmissionConditionLabor(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town, 
			@RequestParam(value = "householdRegistration", required = true) Integer householdRegistration,
			@RequestParam(value = "residence", required = true) Integer residence) {
		
		OperaResult result = new OperaResult();
		AdmissionConditonDto calAdmissionResultAndPropose = new AdmissionConditonDto();
		try
		{
			AdmissionCondition admissionCondition = cacheApplicationService.queryAdmissionConditionBy(serviceBlock, town, householdRegistration, residence);
			//入学结果
			Integer isCanAdmission = admissionCondition.getIsCanAdmission();
			calAdmissionResultAndPropose.setIsCanAdmission(isCanAdmission);
			if (isCanAdmission.intValue() == 2) {
				List<StudentStatusDto> labors = admissionConditionService.queryAdmissionConditionBy(serviceBlock, town, householdRegistration, residence);
				calAdmissionResultAndPropose.setFilterCones(labors);
			}
			result.setResultCode(OperaResult.Success);
			result.setData(calAdmissionResultAndPropose);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		
		return result;
	}
	
	
	/**
	 * 查询入学结果（从居住证条件中）
	 * @param serviceBlock    入学条件所属服务模块         
	 * @param town            条件所属区
	 * @param householdRegistration  标签_户籍
	 * @param residence 标签_居住
	 * @param labor 务工条件
	 * @param residencePermit 居住证
	 * @return
	 */
	@RequestMapping(value="/queryAdmissionConditionResidencePermit", method= {RequestMethod.POST})
	public OperaResult queryAdmissionConditionResidencePermit(@RequestParam(value = "serviceBlock", required = true) Integer serviceBlock,
			@RequestParam(value = "town", required = true) Integer town, 
			@RequestParam(value = "householdRegistration", required = true) Integer householdRegistration,
			@RequestParam(value = "residence", required = true) Integer residence,
			@RequestParam(value = "labor", required = true) Integer labor,
			@RequestParam(value = "residencePermit", required = false) Integer residencePermit) {
		
		OperaResult result = new OperaResult();
		AdmissionConditonDto calAdmissionResultAndPropose = new AdmissionConditonDto();
		try
		{
			 if (residencePermit == null) {
				 List<ConditionLabor> queryLabors = cacheApplicationService.queryLaborBy(serviceBlock, town, householdRegistration, residence, labor);
				 if (queryLabors != null && queryLabors.size() > 0) {
					 ConditionLabor oneLabor =  queryLabors.get(0);
					 calAdmissionResultAndPropose.setIsCanAdmission(oneLabor.getIsCanAdmission());
				 }
			 }else {
				 List<ConditionResidencePermit> conditionResidencePermits = cacheApplicationService.queryConditionResidenPermitBy(serviceBlock, town, 
							householdRegistration, residence, labor, residencePermit);
				 if (conditionResidencePermits != null && conditionResidencePermits.size() > 0) {
					 ConditionResidencePermit onePermit = conditionResidencePermits.get(0);
					 calAdmissionResultAndPropose.setIsCanAdmission(onePermit.getIsCanAdmission());
				 }
			 }
			 
			result.setResultCode(OperaResult.Success);
			result.setData(calAdmissionResultAndPropose);
		}catch(Exception ex) {
			result.setResultCode(OperaResult.Error);
			log.error(ex.getMessage() + ex.fillInStackTrace());
		}
		
		return result;
	}
}
