package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.IndependentRecruitmentQueryLog;
import com.ichzh.physicalFitness.repository.ext.IIndependentRecruitmentQueryLogRepositoryExt;

@Repository
public interface IndependentRecruitmentQueryLogRepository extends BaseRepository<IndependentRecruitmentQueryLog, Integer>,IIndependentRecruitmentQueryLogRepositoryExt{

}
