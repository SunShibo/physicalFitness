package com.ichzh.physicalFitness.service;

import java.util.List;

import com.ichzh.physicalFitness.dto.AdmissionConditonFilterDto;
import com.ichzh.physicalFitness.dto.StudentStatusDto;

public interface AdmissionConditonService {

	/**
	 * 根据入学阶段和区查询入学条件数据的筛选条件.
	 * @param serviceBlock  入学阶段.
	 * @param town   区.
	 * @return
	 */
	public List<AdmissionConditonFilterDto> queryAdmissionConditionFilterData(Integer serviceBlock, Integer town);
	
}
