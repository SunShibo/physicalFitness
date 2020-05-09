package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.PrimSchoolExportQueryLog;
import com.ichzh.physicalFitness.repository.ext.IPrimSchoolExportQueryLogRepositoryExt;

@Repository
public interface PrimSchoolExportQueryLogRepository extends BaseRepository<PrimSchoolExportQueryLog, Integer>, IPrimSchoolExportQueryLogRepositoryExt{

}
