package com.ichzh.physicalFitness.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.Commodity;
import com.ichzh.physicalFitness.model.Member;

public interface RecruitRangeQueryResultRecordService {

	/**
	 * 执行招生范围查询更多限次策略
	 * @param commodity
	 * @param member
	 * @return
	 */
	public OperaResult executeLimitNumStrategy(Commodity commodity, Member member, HttpServletRequest request);
	/**
	 * 招生范围查询更多成功后
	 * @param memberId  会员标识号.
	 * @param schoolCode   学校代码.
	 * @param schoolName  学校名称
	 */
	public void executeAterRecruitRangeQuerySuccess(String memberId, String schoolCode, String schoolName, List<String> addresses);
	
	/**
	 * 为当前会员用豆交换一次查询更多的机会
	 * @param member Member.
	 * @return
	 */
	public OperaResult exchangeOneQuery(Member member);
}
