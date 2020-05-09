package com.ichzh.physicalFitness.repository.ext;

import java.util.List;

import com.ichzh.physicalFitness.model.AdmissionPolicy;

public interface IAdmissionPolicyRepositoryExt {

	/**
	 * 查询入学政策
	 * @param serviceBlock   入学条件所属服务模块
	 * @param town           条件所属区
	 * @return
	 */
	public List<AdmissionPolicy> queryBy(Integer serviceBlock, Integer town);
}
