package com.ichzh.physicalFitness.service;

import javax.servlet.http.HttpServletRequest;

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.Commodity;
import com.ichzh.physicalFitness.model.Member;

public interface PrimSchoolExportQueryRecordService {

	/**
	 * 执行根据学校详情限次策略
	 * @param commodity
	 * @param member
	 * @return
	 */
	public OperaResult executeLimitNumStrategy(Commodity commodity, Member member, HttpServletRequest request);
	/**
	 * 学校详情查询成功后
	 * @param memberId  会员标识号.
	 * @param schoolCode   学校代码.
	 * @param schoolType 学校类型 1:幼儿园 2:中小学
	 * @param schoolName 学校名称
	 */
	public void executeAterSchoolDetailQuerySuccess(String memberId, String schoolCode, Integer schoolType, String schoolName);
	
	/**
	 * 为当前会员用豆交换一次查询机会
	 * @param member Member.
	 * @return
	 */
	public OperaResult exchangeOneQuery(Member member);
	
	/**
	 * 当前会员买一次查询机会
	 * @param memberId  会员标识号.
	 * @return
	 */
	public OperaResult buyOneQuery(String memberId);
}
