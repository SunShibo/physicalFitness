package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.ConditionLabor;
import com.ichzh.physicalFitness.repository.ext.IConditionLaborRepositoryExt;

@Repository
public interface ConditionLaborRepository extends BaseRepository<ConditionLabor, Integer>, IConditionLaborRepositoryExt{

}
