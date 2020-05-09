package com.ichzh.physicalFitness.repository.ext;

import java.util.Date;
import java.util.List;

import com.ichzh.physicalFitness.model.RecruitRangeQueryResultRecord;

public interface IRecruitRangeQueryResultRecordRepositoryExt {

	/**
	 * 查询某个会员某一天的查询记录
	 * @param memberId   会员标识号.
	 * @param recordTime 查询时间.
	 * @return  List<RecruitRangeQueryResultRecord>.
	 */
	public List<RecruitRangeQueryResultRecord> queryByMemberIdAndRecordTime(String memberId, Date recordTime);
}
