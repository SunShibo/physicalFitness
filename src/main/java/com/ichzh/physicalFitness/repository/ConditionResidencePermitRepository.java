package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.ConditionResidencePermit;
import com.ichzh.physicalFitness.repository.ext.IConditionResidencePermitExt;

@Repository
public interface ConditionResidencePermitRepository extends BaseRepository<ConditionResidencePermit, Integer>, IConditionResidencePermitExt{

}
