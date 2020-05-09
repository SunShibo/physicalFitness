package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.SchoolArea;
import com.ichzh.physicalFitness.repository.ext.ISchoolAreaRepositoryExt;

@Repository
public interface SchoolAreaRepository extends BaseRepository<SchoolArea, Integer>, ISchoolAreaRepositoryExt{

}
