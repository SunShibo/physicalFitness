package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.AdmissionTime;
import com.ichzh.physicalFitness.repository.ext.IAdmissionTimeRepositoryExt;

@Repository
public interface AdmissionTimeRepository extends BaseRepository<AdmissionTime, Integer>, IAdmissionTimeRepositoryExt{

}
