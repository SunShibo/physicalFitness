package com.ichzh.physicalFitness.service;

public interface ConditionInfoService {

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
	public boolean updateConditionInfo(String memberId, Integer serviceBlock, Integer town, 
			Integer studentStatus, Integer householdRegistration, Integer residence);
}
