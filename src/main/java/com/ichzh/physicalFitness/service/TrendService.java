package com.ichzh.physicalFitness.service;

import java.util.List;

import com.ichzh.physicalFitness.model.Trend;

public interface TrendService {

	/**
	 * 查询某个学校的趋势数据.
	 * @param serviceBlock  学段 10002 小学  10003 初中
	 * @param schoolCode 学校代码
	 * @return
	 */
	public List<Trend> queryTrendBy(Integer serviceBlock, String schoolCode);
}
