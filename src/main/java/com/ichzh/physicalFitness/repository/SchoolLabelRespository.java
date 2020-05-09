package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.SchoolLabel;
import com.ichzh.physicalFitness.repository.ext.ISchoolLabelRespositoryExt;

@Repository
public interface SchoolLabelRespository  extends BaseRepository<SchoolLabel, Integer>, ISchoolLabelRespositoryExt{

}
