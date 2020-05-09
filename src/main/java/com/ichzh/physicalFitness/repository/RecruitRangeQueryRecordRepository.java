package com.ichzh.physicalFitness.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.RecruitRangeQueryRecord;
import com.ichzh.physicalFitness.repository.ext.IRecruitRangeQueryRecordRepositoryExt;

@Repository
public interface RecruitRangeQueryRecordRepository extends BaseRepository<RecruitRangeQueryRecord, Integer>, IRecruitRangeQueryRecordRepositoryExt{

	/**
	 * 查询招生范围的查询记录.
	 * @param memberId           会员标识号.
	 * @param schoolCode      学校代码.
	 * @return
	 */
	public List<RecruitRangeQueryRecord> findByMemberIdAndSchoolName(String memberId, String schoolName);
	/**
	 * 查询某个会员已经查询的招生范围记录.
	 * @param memberId 会员标识号.
	 * @return List<RecruitRangeQueryRecord>.
	 */
	public List<RecruitRangeQueryRecord> findByMemberId(String memberId);
}
