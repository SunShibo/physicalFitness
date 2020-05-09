package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.JzdSchoolQueryLog;
import com.ichzh.physicalFitness.repository.ext.IJzdSchoolQueryLogRepositoryExt;

@Repository
public interface JzdSchoolQueryLogRepository extends BaseRepository<JzdSchoolQueryLog, Integer>, IJzdSchoolQueryLogRepositoryExt{

}
