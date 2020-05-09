package com.ichzh.physicalFitness.service;

import java.util.Map;

public interface RankingService {

	/**
	 * 查询最新一年排名前5的学校及当前学校排名第几
	 * @param serviecBlock
	 * @param schoolCode
	 * @param town 学校所在区
	 * @return  key selfSort value Integer;  key  top5  value List<Ranking>
	 */
	public Map<String, Object> getTop5RankingAndSelfSortNum(Integer serviecBlock, String schoolCode, Integer town);
}
