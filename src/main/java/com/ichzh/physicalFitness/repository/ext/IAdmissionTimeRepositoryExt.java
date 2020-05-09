package com.ichzh.physicalFitness.repository.ext;

import java.util.List;

import com.ichzh.physicalFitness.model.AdmissionTime;

public interface IAdmissionTimeRepositoryExt {

	/**
	 * 查询入学时间
	 * @param serviceBlock   入学时间所属服务模块
	 * @param town           条件所属区
	 * @return
	 */
	public List<AdmissionTime> queryBy(Integer serviceBlock, Integer town);
}
