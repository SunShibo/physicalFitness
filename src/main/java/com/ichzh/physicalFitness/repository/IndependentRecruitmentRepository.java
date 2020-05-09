package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.IndependentRecruitment;
import com.ichzh.physicalFitness.repository.ext.IIndependentRecruitmentRepositoryExt;

@Repository
public interface IndependentRecruitmentRepository extends BaseRepository<IndependentRecruitment, Integer>,IIndependentRecruitmentRepositoryExt {

}
