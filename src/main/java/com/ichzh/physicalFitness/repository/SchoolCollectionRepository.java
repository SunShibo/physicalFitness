package com.ichzh.physicalFitness.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.SchoolCollection;
import com.ichzh.physicalFitness.repository.ext.ISchoolCollectionRepositoryExt;

@Repository
public interface SchoolCollectionRepository extends BaseRepository<SchoolCollection, Integer>, ISchoolCollectionRepositoryExt{

	public SchoolCollection findByServiceBlockAndMemberIdAndSchoolId(Integer serviceBlock, String memberId, Integer SchoolId);
	
	public List<SchoolCollection> findByServiceBlockAndMemberId(Integer serviceBlock, String memberId);
	
	public List<SchoolCollection> findByMemberId(String memberId);
	
}
