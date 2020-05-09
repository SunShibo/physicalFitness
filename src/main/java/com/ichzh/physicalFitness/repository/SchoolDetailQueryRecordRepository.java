package com.ichzh.physicalFitness.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.SchoolDetailQueryRecord;
import com.ichzh.physicalFitness.repository.ext.ISchoolDetailQueryRecordRepositoryExt;

@Repository
public interface SchoolDetailQueryRecordRepository extends BaseRepository<SchoolDetailQueryRecord, Integer>, ISchoolDetailQueryRecordRepositoryExt{

	/**
	 * 查询学校详情的查询记录.
	 * @param memberId           会员标识号.
	 * @param schoolCode      居住地.
	 * @return
	 */
	public List<SchoolDetailQueryRecord> findByMemberIdAndSchoolCode(String memberId, String schoolCode);
	/**
	 * 查询某个会员已经查询的学校记录.
	 * @param memberId 会员标识号.
	 * @return List<SchoolDetailQueryRecord>.
	 */
	public List<SchoolDetailQueryRecord> findByMemberId(String memberId);
}
