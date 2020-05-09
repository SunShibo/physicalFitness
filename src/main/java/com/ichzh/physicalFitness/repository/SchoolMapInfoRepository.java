package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.SchoolMapInfo;
import com.ichzh.physicalFitness.repository.ext.ISchoolMapInfoRepositoryExt;

@Repository
public interface SchoolMapInfoRepository extends BaseRepository<SchoolMapInfo, Integer>, ISchoolMapInfoRepositoryExt{

}
