package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.AdmissionMode;
import com.ichzh.physicalFitness.repository.ext.IAdmissionModeRepositoryExt;

@Repository
public interface AdmissionModeRepository extends BaseRepository<AdmissionMode, Integer>, IAdmissionModeRepositoryExt{

}
