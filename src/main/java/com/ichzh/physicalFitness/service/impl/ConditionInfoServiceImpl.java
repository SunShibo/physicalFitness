package com.ichzh.physicalFitness.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.model.ConditionInfo;
import com.ichzh.physicalFitness.repository.ConditionInfoRepository;
import com.ichzh.physicalFitness.service.ConditionInfoService;

@Service("conditionInfoService")
public class ConditionInfoServiceImpl implements ConditionInfoService {
	
	@Autowired
	ConditionInfoRepository  conditionInfoRepository;

	/**
	 * 保存某个会员的筛选条件
	 * @param memberId        会员ID.
	 * @param serviceBlock    服务模块.
	 * @param town            入学区域.
	 * @param studentStatus   学籍所在地
	 * @param householdRegistration  户籍所在地
	 * @param residence  居住地
	 * @return
	 */
	public boolean updateConditionInfo(String memberId, Integer serviceBlock, Integer town, Integer studentStatus,
			Integer householdRegistration, Integer residence) {
		
		ConditionInfo conditionInfo = conditionInfoRepository.findByServiceBlockAndMemberId(serviceBlock, memberId);
		if (conditionInfo == null) {
			conditionInfo = new ConditionInfo();
		}
		conditionInfo.setServiceBlock(serviceBlock);
		conditionInfo.setMemberId(memberId);
		conditionInfo.setTown(town);
		conditionInfo.setXjAddress(studentStatus);
		conditionInfo.setHjAddress(householdRegistration);
		conditionInfo.setJzdAddress(residence);
		conditionInfoRepository.saveAndFlush(conditionInfo);
		
		return true;
	}

}
