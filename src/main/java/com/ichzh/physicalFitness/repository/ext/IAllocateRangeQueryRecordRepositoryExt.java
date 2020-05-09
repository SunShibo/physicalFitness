package com.ichzh.physicalFitness.repository.ext;

import java.util.Date;
import java.util.List;

import com.ichzh.physicalFitness.model.AllocateRangeQueryRecord;

public interface IAllocateRangeQueryRecordRepositoryExt {

	/**
	 * 查询某个会员某一天的查询记录
	 * @param memberId   会员标识号.
	 * @param recordTime 查询时间.
	 * @return  List<JzdSchoolQueryRecord>.
	 */
	public List<AllocateRangeQueryRecord> queryByMemberIdAndRecordTime(String memberId, Date recordTime);
}
