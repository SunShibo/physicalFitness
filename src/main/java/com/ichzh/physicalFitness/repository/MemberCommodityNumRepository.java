package com.ichzh.physicalFitness.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.MemberCommodityNum;
import com.ichzh.physicalFitness.repository.ext.IMemberCommodityNumRepositoryExt;

@Repository
public interface MemberCommodityNumRepository extends BaseRepository<MemberCommodityNum, Integer>, IMemberCommodityNumRepositoryExt{

	/**
	 * 查询某个会员某个api的可使用次数
	 * @param memberId  会员标识号.
	 * @param apiCode   apiCode
	 * @return List<MemberCommodityNum>.
	 */
	public List<MemberCommodityNum> findByMemberIdAndApiCode(String memberId, Integer apiCode);
}
