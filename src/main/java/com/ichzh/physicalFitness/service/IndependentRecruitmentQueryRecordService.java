package com.ichzh.physicalFitness.service;

import javax.servlet.http.HttpServletRequest;

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.Commodity;
import com.ichzh.physicalFitness.model.Member;

public interface IndependentRecruitmentQueryRecordService {

	/**
	 * 执行根据区查询自主招生限次策略
	 * @param commodity
	 * @param member
	 * @return
	 */
	public OperaResult executeLimitNumStrategy(Commodity commodity, Member member, HttpServletRequest request);
	/**
	 * 自主招生查询成功后
	 * @param memberId  会员标识号.
	 * @param address   查询的居住地.
	 */
	public void executeAterIndependentRecruitmentQuerySuccess(String memberId, String address);
	
	/**
	 * 为当前会员用豆交换一次查询机会
	 * @param member Member.
	 * @return
	 */
	public OperaResult exchangeOneQuery(Member member);
}
