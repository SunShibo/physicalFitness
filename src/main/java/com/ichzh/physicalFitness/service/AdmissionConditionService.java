package com.ichzh.physicalFitness.service;

import java.util.List;

import com.ichzh.physicalFitness.dto.AdmissionConditonDto;
import com.ichzh.physicalFitness.dto.StudentStatusDto;
import com.ichzh.physicalFitness.model.AdmissionCondition;

public interface AdmissionConditionService {

	/**
	 * 用两个井号分隔条件详情
	 * @param adminCondition  List<AdmissionCondition>
	 */
	public void spitConditionDetail(List<AdmissionCondition> adminConditiones);
	/**
	 * 根据入学条件查询结果，给出入学资格结论和建议。
	 * @param adminConditiones List<AdmissionCondition>.
	 * @return
	 */
	public AdmissionConditonDto calAdmissionResultAndPropose(List<AdmissionCondition> adminConditiones);
	
	
	/**
     * 根据入学阶段、区、户籍、居住查询入学条件 
     * @param serviceBlock
     * @param town
     * @param householdRegistration  户籍
     * @param residence   居住 
     * @return
     */
    public List<StudentStatusDto> queryAdmissionConditionBy(Integer serviceBlock, Integer town, Integer householdRegistration, Integer residence);
	
}
