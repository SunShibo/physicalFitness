package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.AdmissionPolicy;
import com.ichzh.physicalFitness.repository.ext.IAdmissionPolicyRepositoryExt;

@Repository
public interface AdmissionPolicyRepository extends BaseRepository<AdmissionPolicy, Integer>, IAdmissionPolicyRepositoryExt{

}
