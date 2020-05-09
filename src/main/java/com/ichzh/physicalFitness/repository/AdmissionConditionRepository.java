package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.AdmissionCondition;
import com.ichzh.physicalFitness.repository.ext.IAdmissionConditionRepositoryExt;

@Repository
public interface AdmissionConditionRepository extends BaseRepository<AdmissionCondition, Integer>, IAdmissionConditionRepositoryExt{

}
