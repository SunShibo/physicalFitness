package com.ichzh.physicalFitness.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.AllocateRangeQueryRecord;
import com.ichzh.physicalFitness.repository.ext.IAllocateRangeQueryRecordRepositoryExt;

@Repository
public interface AllocateRangeQueryRecordRepository extends BaseRepository<AllocateRangeQueryRecord, Integer>, IAllocateRangeQueryRecordRepositoryExt {

	/**
	 * 查询派位范围的查询记录.
	 * @param memberId           会员标识号.
	 * @param schoolCode      学校代码.
	 * @return
	 */
	public List<AllocateRangeQueryRecord> findByMemberIdAndSchoolCode(String memberId, String schoolCode);
	/**
	 * 查询某个会员已经查询的派位记录.
	 * @param memberId 会员标识号.
	 * @return List<AllocateRangeQueryRecord>.
	 */
	public List<AllocateRangeQueryRecord> findByMemberId(String memberId);
}
