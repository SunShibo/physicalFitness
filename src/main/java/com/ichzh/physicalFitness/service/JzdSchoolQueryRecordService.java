package com.ichzh.physicalFitness.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.Commodity;
import com.ichzh.physicalFitness.model.JzdSchoolQueryRecord;
import com.ichzh.physicalFitness.model.Member;

public interface JzdSchoolQueryRecordService {

	/**
	 * 执行根据居住地查询学校限次策略
	 * @param commodity
	 * @param member
	 * @return
	 */
	public OperaResult executeLimitNumStrategy(Commodity commodity, Member member, HttpServletRequest request);
	/**
	 * 居住地查询成功后
	 * @param memberId  会员标识号.
	 * @param address   查询的居住地.
	 */
	public void executeAterJzdSchoolQuerySuccess(String memberId, String address, Integer serviceBlock, Integer town);
	
	/**
	 * 为当前会员用豆交换一次查询机会
	 * @param member Member.
	 * @return
	 */
	public OperaResult exchangeOneQuery(Member member);
	/**
	 * 设置区名称
	 * @param jzdSChoolQueryRecords
	 */
	public void setTownName(List<JzdSchoolQueryRecord> jzdSChoolQueryRecords);
}
