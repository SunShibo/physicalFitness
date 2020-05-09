package com.ichzh.physicalFitness.repository.ext;

public interface IMemberRepositoryExt {

	/**
	 * 更新手机号
	 * @param memberId  会员标识号。
	 * @param phone  手机号
	 * @return
	 */
	boolean updateMemberPhone(String memberId, String phone);

}
