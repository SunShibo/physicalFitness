package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.AllocateRangeQueryLog;
import com.ichzh.physicalFitness.repository.ext.IAllocateRangeQueryLogRepositoryExt;

@Repository
public interface AllocateRangeQueryLogRepository extends BaseRepository<AllocateRangeQueryLog, Integer>, IAllocateRangeQueryLogRepositoryExt {

}
