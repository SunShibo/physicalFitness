package com.ichzh.physicalFitness.repository.ext;

import java.util.List;

import com.ichzh.physicalFitness.model.ConditionInfo;

public interface IConditionInfoRepositoryExt {

	/**
	 * 查询某个会员所有的筛选条件
	 * @param memberId   会员ID
	 * @return
	 */
	public List<ConditionInfo> queryBy(String memberId);
	

}
