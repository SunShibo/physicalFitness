package com.ichzh.physicalFitness.repository;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.EnrollmentSchedule;
import com.ichzh.physicalFitness.repository.ext.IEnrollmentScheduleRepositoryExt;

@Repository
public interface EnrollmentScheduleRepository extends BaseRepository<EnrollmentSchedule, Integer>, IEnrollmentScheduleRepositoryExt {

}
