package com.ichzh.physicalFitness.repository.ext;

import com.ichzh.physicalFitness.model.Member;

public interface IMemberShareRepositoryExt {
	
	public void updateMemberShare(String memberId, String shareUserId);
	
	public void updateMemberSharePay(String memberId);
	
	public void updateMemberShareBangdingPhoneNumber(Member member);
}
