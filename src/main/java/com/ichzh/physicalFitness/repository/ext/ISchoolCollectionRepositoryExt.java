package com.ichzh.physicalFitness.repository.ext;

public interface ISchoolCollectionRepositoryExt {

	public void deleteByServiceBlockAndMemberIdAndSchoolId(Integer serviceBlock, String memberId, Integer SchoolId);
}
