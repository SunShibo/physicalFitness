package com.ichzh.physicalFitness.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.PrimSchoolExportQueryRecord;
import com.ichzh.physicalFitness.repository.ext.IPrimSchoolExportQueryRecordRepositoryExt;

@Repository
public interface PrimSchoolExportQueryRecordRepository extends BaseRepository<PrimSchoolExportQueryRecord, Integer>, IPrimSchoolExportQueryRecordRepositoryExt{

	/**
	 * 查询学校出口的查询记录.
	 * @param memberId           会员标识号.
	 * @param schoolCode      小学学校代码.
	 * @return
	 */
	public List<PrimSchoolExportQueryRecord> findByMemberIdAndSchoolCode(String memberId, String schoolCode);
	/**
	 * 查询某个会员已经查询的学校出口记录.
	 * @param memberId 会员标识号.
	 * @return List<PrimSchoolExportQueryRecord>.
	 */
	public List<PrimSchoolExportQueryRecord> findByMemberId(String memberId);
}
