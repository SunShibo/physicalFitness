package com.ichzh.physicalFitness.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.JzdSchoolQueryRecord;
import com.ichzh.physicalFitness.repository.ext.IJzdSchoolQueryRecordRepositoryExt;

@Repository
public interface JzdSchoolQueryRecordRepository extends BaseRepository<JzdSchoolQueryRecord, Integer>, IJzdSchoolQueryRecordRepositoryExt{

	/**
	 * 查询居住地对应学校的查询记录.
	 * @param memberId           会员标识号.
	 * @param detailAddress      居住地.
	 * @return
	 */
	public List<JzdSchoolQueryRecord> findByMemberIdAndDetailAddress(String memberId, String detailAddress);
	
	public List<JzdSchoolQueryRecord> findByMemberId(String memberId);
}
