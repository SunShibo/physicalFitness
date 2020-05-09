package com.ichzh.physicalFitness.service;

import java.util.List;

import com.ichzh.physicalFitness.model.SchoolCollection;

public interface SchoolCollectionService {

	/**
	 * 收藏或取消收藏一个学校
	 * @param memberId        会员ID.
	 * @param serviceBlock    服务模块.
	 * @param schooId     
	 * @param collectionOrCancel  操作类型    .1: 收藏  0：取消
	 * @return
	 */
	public boolean collectionOrCancel(String memberId, Integer serviceBlock, Integer schooId, Integer collectionOrCancel);
	
	/**
	 * 设置学校名称
	 * @param schoolCollections
	 */
	public void setSchoolName(List<SchoolCollection> schoolCollections);
	
}
