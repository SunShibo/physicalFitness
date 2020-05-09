package com.ichzh.physicalFitness.service;

import javax.servlet.http.HttpServletRequest;

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.Commodity;
import com.ichzh.physicalFitness.model.Member;

public interface RecruitRangeQueryRecordService {

	/**
	 * 执行招生范围查询限次策略
	 * @param commodity
	 * @param member
	 * @return
	 */
	public OperaResult executeLimitNumStrategy(Commodity commodity, Member member, HttpServletRequest request);
	/**
	 * 招生范围查询成功后
	 * @param memberId  会员标识号.
	 * @param schoolCode   学校代码.
	 * @param schoolName  学校名称
	 */
	public void executeAterRecruitRangeQuerySuccess(String memberId, String schoolCode, String schoolName, Integer serviceBlock, Integer town);
	
	/**
	 * 为当前会员用豆交换一次查询机会
	 * @param member Member.
	 * @return
	 */
	public OperaResult exchangeOneQuery(Member member);
}
