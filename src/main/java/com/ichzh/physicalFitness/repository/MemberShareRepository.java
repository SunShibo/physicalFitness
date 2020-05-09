package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.MemberShare;
import com.ichzh.physicalFitness.repository.ext.IMemberShareRepositoryExt;

@Repository
public interface MemberShareRepository extends BaseRepository<MemberShare, Integer>, IMemberShareRepositoryExt{
	
	public MemberShare findByOpenIdAndIsCalculated(String openId, Integer isCalculated);
}
