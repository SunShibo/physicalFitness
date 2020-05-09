package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.RecruitRangeQueryLog;
import com.ichzh.physicalFitness.repository.ext.IRecruitRangeQueryLogRepositoryExt;

@Repository
public interface RecruitRangeQueryLogRepository extends BaseRepository<RecruitRangeQueryLog, Integer>, IRecruitRangeQueryLogRepositoryExt{

}
