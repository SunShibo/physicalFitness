package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.JzdSchool;
import com.ichzh.physicalFitness.repository.ext.IJzdSchoolRepositoryExt;

@Repository
public interface JzdSchoolRepository extends BaseRepository<JzdSchool, Integer>, IJzdSchoolRepositoryExt{

}
