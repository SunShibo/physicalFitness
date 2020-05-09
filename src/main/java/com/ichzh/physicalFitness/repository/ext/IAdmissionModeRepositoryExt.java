package com.ichzh.physicalFitness.repository.ext;

import java.util.List;

import com.ichzh.physicalFitness.model.AdmissionMode;

public interface IAdmissionModeRepositoryExt {

	/**
	 * 查询入学方式
	 * @param serviceBlock   入学条件所属服务模块
	 * @param town           条件所属区
	 * @return
	 */
	public List<AdmissionMode> queryBy(Integer serviceBlock, Integer town);
}
