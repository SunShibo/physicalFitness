package com.ichzh.physicalFitness.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.ComparisonSchool;
import com.ichzh.physicalFitness.repository.ext.IComparisonSchoolRepositoryExt;

@Repository
public interface ComparisonSchoolRepository  extends BaseRepository<ComparisonSchool, Integer>, IComparisonSchoolRepositoryExt{

	public List<ComparisonSchool> findByMemberId(String memberId);
	
	public List<ComparisonSchool> findByMemberIdAndSchoolTypeAndSchoolId(String memberId, Integer schoolType, Integer schoolId);
}
