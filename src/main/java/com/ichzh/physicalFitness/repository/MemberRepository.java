package com.ichzh.physicalFitness.repository;


import org.springframework.stereotype.Repository;

import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.repository.ext.IMemberRepositoryExt;

/**
 * 用户dao
 * @author xqx
 */
@Repository
public interface MemberRepository extends BaseRepository<Member, String>, IMemberRepositoryExt {

	Member findByMemberUsername(String memberUsername);

	Member findByMemberWeChat(String memberWeChat);

	Member findByMemberId(String memberId);
}
