package com.ichzh.physicalFitness.repository.ext;

import java.util.List;

import com.ichzh.physicalFitness.model.UnifiedAccount;

public interface IUnifiedAccountRepositoryExt {

	/**
	 * 根据小程序openId获取公众号openId
	 * @param wechatOpenId  小程序openId
	 * @return
	 */
	public List<UnifiedAccount> queryUnifiedAccountBy(String wechatOpenId);
}
