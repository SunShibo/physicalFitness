package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.NurserySchool;
import com.ichzh.physicalFitness.repository.ext.INurserySchoolRepositoryExt;

@Repository
public interface NurserySchoolRepository extends BaseRepository<NurserySchool, Integer>,INurserySchoolRepositoryExt{

}
