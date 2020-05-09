package com.ichzh.physicalFitness.repository.ext;

import java.util.List;

import com.ichzh.physicalFitness.model.IndependentRecruitment;

public interface IIndependentRecruitmentRepositoryExt {

	/**
	 * 查询自主招生
	 * @param serviceBlock  自主招生所属服务模块
	 * @param town          自主招生所属区
	 * @param isCity        是否全市招生
	 * @return
	 */
	public List<IndependentRecruitment> queryBy(Integer serviceBlock, Integer town, Integer isCity);
	
}
