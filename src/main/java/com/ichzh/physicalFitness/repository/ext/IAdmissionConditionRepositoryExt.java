package com.ichzh.physicalFitness.repository.ext;

import java.util.List;

import com.ichzh.physicalFitness.dto.StudentStatusDto;
import com.ichzh.physicalFitness.model.AdmissionCondition;

public interface IAdmissionConditionRepositoryExt {

	/**
	 * 查询入学条件
	 * @param serviceBlock   入学条件所属服务模块
	 * @param town           条件所属区
	 * @param studentStatus  标签_学籍
	 * @param householdRegistration 标签_户籍
	 * @param residence  标签_居住
	 * @return
	 */
	public List<AdmissionCondition> queryBy(Integer serviceBlock, Integer town, 
			Integer studentStatus, Integer householdRegistration, Integer residence);
	
	/**
	 * 查询搜索条件中的学籍筛选数据
	 * @param serviceBlock   入学条件所属服务模块
	 * @param town           条件所属区
	 * @return
	 */
	public List<StudentStatusDto> queryStudentStatus(Integer serviceBlock, Integer town);
	
	/**
	 * 查询搜索条件中的户籍筛选数据
	 * @param serviceBlock   入学条件所属服务模块
	 * @param town           条件所属区
	 * @param studentStatus  标签_学籍
	 * @return
	 */
	public List<StudentStatusDto> queryHouseholdRegistration(Integer serviceBlock, Integer town, Integer  studentStatus);
	
	/**
	 * 查询搜索条件中的居住地筛选数据
	 * @param serviceBlock   入学条件所属服务模块
	 * @param town           条件所属区
	 * @param studentStatus  标签_学籍
	 * @param householdRegistration 标签_户籍
	 * @return
	 */
	public List<StudentStatusDto> queryResidence(Integer serviceBlock, Integer town, Integer  studentStatus, Integer householdRegistration);
}
