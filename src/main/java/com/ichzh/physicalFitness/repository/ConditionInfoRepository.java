package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.ConditionInfo;
import com.ichzh.physicalFitness.repository.ext.IConditionInfoRepositoryExt;

@Repository
public interface ConditionInfoRepository extends BaseRepository<ConditionInfo, Integer>, IConditionInfoRepositoryExt{

	public ConditionInfo findByServiceBlockAndMemberId(Integer serviceBlock, String memberId);
	

}
