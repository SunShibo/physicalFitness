package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.EnrollmentScheduleList;
import com.ichzh.physicalFitness.repository.ext.IEnrollmentScheduleListRepositoryExt;

@Repository
public interface EnrollmentScheduleListRepository extends BaseRepository<EnrollmentScheduleList, Integer>, IEnrollmentScheduleListRepositoryExt{

}
