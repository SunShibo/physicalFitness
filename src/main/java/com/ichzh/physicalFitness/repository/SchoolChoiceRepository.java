package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.SchoolChoice;
import com.ichzh.physicalFitness.repository.ext.ISchoolChoiceRepositoryExt;

@Repository
public interface SchoolChoiceRepository extends BaseRepository<SchoolChoice, Integer>,ISchoolChoiceRepositoryExt {

}
