package com.ichzh.physicalFitness.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.conf.Constant;
import com.ichzh.physicalFitness.model.MemberCommodityNum;
import com.ichzh.physicalFitness.repository.CommodityRepository;
import com.ichzh.physicalFitness.repository.MemberCommodityNumRepository;
import com.ichzh.physicalFitness.service.MemberCommodityNumService;
import com.yodoo.spring.dynamicquery.BaseRepositoryExtImpl;

@Service("memberCommodityNumService")
public class MemberCommodityNumServiceImpl extends BaseRepositoryExtImpl implements MemberCommodityNumService {
	
	@Autowired
	MemberCommodityNumRepository memberCommodityNumRepository;
	@Autowired
	CommodityRepository commodityRepository;

	/**
	 * 查询某个会员某个api的可使用次数
	 * @param memberId  会员标识号.
	 * @param apiCode   apiCode
	 * @return List<MemberCommodityNum>.
	 */
	public MemberCommodityNum queryByMemberIdAndApiCode(String memberId, Integer apiCode) {
		
		
		List<MemberCommodityNum> memberCommNums = memberCommodityNumRepository.findByMemberIdAndApiCode(memberId, apiCode);
		//如果没有则初始化一条数据
		if (memberCommNums == null || memberCommNums.size() == 0) {
			MemberCommodityNum memCommNum = new MemberCommodityNum();
			memCommNum.setMemberId(memberId);
			memCommNum.setApiCode(apiCode);
			memCommNum.setBuyExchangeNum(0);
			
			memCommNum.setFreeNum(commodityRepository.queryFreeNumOfCommodity(apiCode));
			
			
			memberCommodityNumRepository.saveAndFlush(memCommNum);
			
			return memCommNum;
		}else {
			return memberCommNums.get(0);
		}
	}

	
}
