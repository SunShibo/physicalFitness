package com.ichzh.physicalFitness.service;

import com.ichzh.physicalFitness.model.MemberCommodityNum;

public interface MemberCommodityNumService {

	/**
	 * 查询某个会员某个api的可使用次数
	 * @param memberId  会员标识号.
	 * @param apiCode   apiCode
	 * @return List<MemberCommodityNum>.
	 */
	public MemberCommodityNum queryByMemberIdAndApiCode(String memberId, Integer apiCode);
}
