package com.ichzh.physicalFitness.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.conf.Constant;
import com.ichzh.physicalFitness.dto.AdmissionConditonFilterDto;
import com.ichzh.physicalFitness.repository.AdmissionConditionRepository;
import com.ichzh.physicalFitness.service.AdmissionConditonService;

@Service("admissionConditonService")
public class AdmissionConditonServiceImpl implements AdmissionConditonService {

	@Autowired
	AdmissionConditionRepository admissionConditionRepository;
	
	
	/**
	 * 根据入学阶段和区查询入学条件数据的筛选条件.
	 * @param serviceBlock  入学阶段.
	 * @param town   区.
	 * @return
	 */
	public List<AdmissionConditonFilterDto> queryAdmissionConditionFilterData(Integer serviceBlock, Integer town) {
		
		//上中学
		if (Constant.DICT_ID_10003 == serviceBlock.intValue()) {
			//查询学籍筛选条件
			
		}
		return null;
	}

	
}
