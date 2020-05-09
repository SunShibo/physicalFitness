package com.ichzh.physicalFitness.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.RecruitRangeQueryResultRecord;
import com.ichzh.physicalFitness.repository.ext.IRecruitRangeQueryResultRecordRepositoryExt;

@Repository
public interface RecruitRangeQueryResultRecordRepository extends BaseRepository<RecruitRangeQueryResultRecord, Integer>, 
	IRecruitRangeQueryResultRecordRepositoryExt{

	/**
	 * 查询某个会员已经查询过的某个学校的招生地址
	 * @param memberId   会员标识号.
	 * @param schoolName 学校名称
	 * @return
	 */
	public List<RecruitRangeQueryResultRecord> findByMemberIdAndSchoolName(String memberId, String schoolName);
}
