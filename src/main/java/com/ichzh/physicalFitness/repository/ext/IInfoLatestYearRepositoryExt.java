package com.ichzh.physicalFitness.repository.ext;

import java.util.List;

import com.ichzh.physicalFitness.model.InfoLatestYear;

public interface IInfoLatestYearRepositoryExt {

	/**
	 * 查询每个功能编号信息更新年度信息
	 * @param serviceBlock   入学条件所属服务模块
	 * @param town           条件所属区
	 * @return
	 */
	public List<InfoLatestYear> queryBy(Integer serviceBlock, Integer town);
}
