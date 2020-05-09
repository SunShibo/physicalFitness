package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.SchoolDetailQueryLog;
import com.ichzh.physicalFitness.repository.ext.ISchoolDetailQueryLogRepositoryExt;

@Repository
public interface SchoolDetailQueryLogRepository extends BaseRepository<SchoolDetailQueryLog, Integer>, ISchoolDetailQueryLogRepositoryExt{

}
