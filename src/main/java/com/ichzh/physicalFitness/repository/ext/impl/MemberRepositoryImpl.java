package com.ichzh.physicalFitness.repository.ext.impl;

import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.repository.MemberRepository;
import com.ichzh.physicalFitness.repository.ext.IMemberRepositoryExt;
import com.yodoo.spring.dynamicquery.BaseRepositoryExtImpl;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class MemberRepositoryImpl extends BaseRepositoryExtImpl implements IMemberRepositoryExt {
	
	@Autowired
	public MemberRepository memberRepository;

	/**
	 * 更新手机号
	 * @param memberId  会员标识号。
	 * @param phone  手机号
	 * @return
	 */
	public boolean updateMemberPhone(String memberId, String phone) {
		
		if (StringUtils.isNotEmpty(memberId)) {
			Member member = memberRepository.findByMemberId(memberId);
			if (member != null && StringUtils.isNotEmpty(phone)) {
				member.setMemberMobile(phone);
				memberRepository.save(member);
				
				return true;
			}
		}else {
			log.warn("memberId 为空");
		}
		return false;
	}
	
	
	
}
 