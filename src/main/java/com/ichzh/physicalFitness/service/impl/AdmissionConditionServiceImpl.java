package com.ichzh.physicalFitness.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.conf.Constant;
import com.ichzh.physicalFitness.dto.AdmissionConditonDto;
import com.ichzh.physicalFitness.dto.StudentStatusDto;
import com.ichzh.physicalFitness.model.AdmissionCondition;
import com.ichzh.physicalFitness.model.ConditionLabor;
import com.ichzh.physicalFitness.model.SysDict;
import com.ichzh.physicalFitness.service.AdmissionConditionService;
import com.ichzh.physicalFitness.service.ICacheApplicationService;

@Service("admissionConditionService")
public class AdmissionConditionServiceImpl implements AdmissionConditionService {

	@Autowired
	ICacheApplicationService cacheApplicationService;
	
	/**
	 * 用两个井号分隔条件详情
	 * @param adminCondition  List<AdmissionCondition>
	 */
	public void spitConditionDetail(List<AdmissionCondition> adminConditiones) {
		
		if (adminConditiones != null && adminConditiones.size() > 0) {
			for (AdmissionCondition oneCondition : adminConditiones) {
				String conditionDetail = oneCondition.getConditionDetail();
				if (StringUtils.isNotEmpty(conditionDetail)) {
					String[] arrConditionDetail = conditionDetail.split("##");
					oneCondition.setLstConditionDesc(Arrays.asList(arrConditionDetail));   
				}
			}
		}
	}

	/**
	 * 根据入学条件查询结果，给出入学资格结论和建议。
	 * @param adminConditiones List<AdmissionCondition>.
	 * @return
	 */
	public AdmissionConditonDto calAdmissionResultAndPropose(List<AdmissionCondition> adminConditiones) {
		
		AdmissionConditonDto ret = new AdmissionConditonDto();
		if (adminConditiones == null || adminConditiones.size() == 0) {
			ret.setIsCanAdmission(0);
			return ret;
		}
		//加入有入学资格
		if (checkIfCanAdmission(adminConditiones)) {
			ret.setIsCanAdmission(1);
		}else {
			ret.setIsCanAdmission(0);
			//上小学
			AdmissionCondition currentAdmissionCondition = adminConditiones.get(0);
			if (currentAdmissionCondition.getServiceBlock().intValue() == Constant.DICT_ID_10002) {
				//建议
				ret.setChangeConditionKind(calPropose4PrimSchool(currentAdmissionCondition));
			}
			// 上中学
			if (currentAdmissionCondition.getServiceBlock().intValue() == Constant.DICT_ID_10003) {
				//建议
				ret.setChangeConditionKind(calPropose4MiddleSchool(currentAdmissionCondition));
			} 
		}
		return ret;
	}
	
	//给出获得入学资格建议
	private List<String> calPropose4PrimSchool(AdmissionCondition admissionConditon){
		List<String> ret = new ArrayList<String>();
		try
		{
			//入学阶段
			Integer serviceBlock = admissionConditon.getServiceBlock();
			//当前选择的入学区域
			Integer town = admissionConditon.getTown();
			//当前选择的户籍
			Integer householdRegistration = admissionConditon.getHouseholdRegistration();
			//当前选择的居住地
			Integer residence = admissionConditon.getResidence();
			
			//户籍字典
			List<SysDict> householdRegistrationDict = cacheApplicationService.getDictByDictType(Constant.DICT_TYPE_104);
			//居住地字典
			List<SysDict> residenceDict = cacheApplicationService.getDictByDictType(Constant.DICT_TYPE_105);
			
			//所有具备入学资格的组合
			List<String> allStandardCombination = getConditionCombination(serviceBlock, town);
			
			//尝试改变户籍
			for (SysDict oneHouseHoldReg :  householdRegistrationDict) {
				//户籍地
				Integer currentHouseHold = oneHouseHoldReg.getDictId();
				if (!householdRegistration.equals(currentHouseHold+"")) {
					//新的获取入学资格的组合
					String newAdmissionCondCombination = serviceBlock+"#"+town+"#"+currentHouseHold+"#"+residence;
					if (allStandardCombination.contains(newAdmissionCondCombination)) {
						ret.add(cacheApplicationService.getDictTypeName(Constant.DICT_TYPE_104));
						break;
					}
				}
			}
			//尝试改变居住地
			for (SysDict oneResidence :  residenceDict) {
				//居住地
				Integer currentResidence = oneResidence.getDictId();
				if (!residence.equals(currentResidence+"")) {
					//新的获取入学资格的组合
					String newAdmissionCondCombination = serviceBlock+"#"+town+"#"+householdRegistration+"#"+currentResidence;
					if (allStandardCombination.contains(newAdmissionCondCombination)) {
						ret.add(cacheApplicationService.getDictTypeName(Constant.DICT_TYPE_105));
						break;
					}
				}
			}
			
			
		}catch(Exception ex) {
			//不用处理
		}
		return ret;
	}
	
	//给出获得入学资格建议——上中学
	private List<String> calPropose4MiddleSchool(AdmissionCondition admissionConditon){
		List<String> ret = new ArrayList<String>();
		try
		{
			//入学阶段
			Integer serviceBlock = admissionConditon.getServiceBlock();
			//当前选择的入学区域
			Integer town = admissionConditon.getTown();
			//当前选择的学籍
			Integer studentStatus = admissionConditon.getStudentStatus();
			//当前选择的户籍
			Integer householdRegistration = admissionConditon.getHouseholdRegistration();
			//当前选择的居住地
			Integer residence = admissionConditon.getResidence();
			
			//学籍字典
			List<SysDict> studentStatusDict = cacheApplicationService.getDictByDictType(Constant.DICT_TYPE_103);
			//户籍字典
			List<SysDict> householdRegistrationDict = cacheApplicationService.getDictByDictType(Constant.DICT_TYPE_104);
			//居住地字典
			List<SysDict> residenceDict = cacheApplicationService.getDictByDictType(Constant.DICT_TYPE_105);
			
			//所有具备入学资格的组合
			List<String> allStandardCombination = getConditionCombination(serviceBlock, town);
			
			//尝试改变学籍
			for (SysDict oneStudentStatus :  studentStatusDict) {
				//学籍
				Integer currentStudentStatus = oneStudentStatus.getDictId();
				if (!studentStatus.equals(currentStudentStatus+"")) {
					//新的获取入学资格的组合
					String newAdmissionCondCombination = serviceBlock+"#"+town+"#"+currentStudentStatus+"#"+householdRegistration+"#"+residence;
					if (allStandardCombination.contains(newAdmissionCondCombination)) {
						ret.add(cacheApplicationService.getDictTypeName(Constant.DICT_TYPE_103));
						break;
					}
				}
			}
			
			//尝试改变户籍
			for (SysDict oneHouseHoldReg :  householdRegistrationDict) {
				//户籍地
				Integer currentHouseHold = oneHouseHoldReg.getDictId();
				if (!householdRegistration.equals(currentHouseHold+"")) {
					//新的获取入学资格的组合
					String newAdmissionCondCombination = serviceBlock+"#"+town+"#"+studentStatus+"#"+currentHouseHold+"#"+residence;
					if (allStandardCombination.contains(newAdmissionCondCombination)) {
						ret.add(cacheApplicationService.getDictTypeName(Constant.DICT_TYPE_104));
						break;
					}
				}
			}
			//尝试改变居住地
			for (SysDict oneResidence :  residenceDict) {
				//居住地
				Integer currentResidence = oneResidence.getDictId();
				if (!residence.equals(currentResidence+"")) {
					//新的获取入学资格的组合
					String newAdmissionCondCombination = serviceBlock+"#"+town+"#"+studentStatus+"#"+householdRegistration+"#"+currentResidence;
					if (allStandardCombination.contains(newAdmissionCondCombination)) {
						ret.add(cacheApplicationService.getDictTypeName(Constant.DICT_TYPE_105));
						break;
					}
				}
			}
			
			
		}catch(Exception ex) {
			//不用处理
		}
		return ret;
	}
	
	//根据入学阶段获取所有具备入学资格的条件组合
	private List<String> getConditionCombination(Integer serviceBlock, Integer town){
		
		List<String> ret = new ArrayList<String>();
		try
		{
			//上小学
			if (serviceBlock.intValue() == Constant.DICT_ID_10002) {
				ret = cacheApplicationService.getAdmissionConditionCombination4primSchool(town);
			}else if(serviceBlock.intValue() == Constant.DICT_ID_10003) {  //上中学
				ret = cacheApplicationService.getAdmissionConditionCombination4MiddleSchool(town);
			}
		}catch(Exception ex) {
			//不用处理
		}
		
		return ret;
	}
	
	//检测是否有入学资格
	private boolean checkIfCanAdmission(List<AdmissionCondition> adminConditiones) {
		boolean ret = false;
		if (adminConditiones != null && adminConditiones.size() > 0) {
			for (AdmissionCondition oneCondition : adminConditiones) {
				Integer ifCanAdmission = oneCondition.getIsCanAdmission();
				if (ifCanAdmission != null  && ifCanAdmission.intValue() == 1) {
					ret = true;
					break;
				}
			}
		}
		return ret;
	}

	
	/**
     * 根据入学阶段、区、户籍、居住查询入学条件 
     * @param serviceBlock
     * @param town
     * @param householdRegistration  户籍
     * @param residence   居住 
     * @return
     */
	public List<StudentStatusDto> queryAdmissionConditionBy(Integer serviceBlock, Integer town,
			Integer householdRegistration, Integer residence) {
		
		List<StudentStatusDto> ret = new ArrayList<StudentStatusDto>();
		AdmissionCondition admissionCondition = cacheApplicationService.queryAdmissionConditionBy(serviceBlock, town, householdRegistration, residence);
		if (admissionCondition != null) {
			//入学结果
			Integer isCanAdmission = admissionCondition.getIsCanAdmission();
			//按务工条件查询
			if (isCanAdmission != null && isCanAdmission.intValue() == 2) {
				List<ConditionLabor> conLabors = cacheApplicationService.queryLaborBy(serviceBlock, town, householdRegistration, residence);
				if (conLabors != null && conLabors.size() > 0) {
					for (ConditionLabor oneLabor :  conLabors) {
						StudentStatusDto oneStatus = new StudentStatusDto();
						oneStatus.setDictId(oneLabor.getLabor());
						oneStatus.setDictName(cacheApplicationService.getDictName(oneLabor.getLabor()));
						ret.add(oneStatus);
					}
				}
			}
		}
		return ret;
	}
	
	

	
}
