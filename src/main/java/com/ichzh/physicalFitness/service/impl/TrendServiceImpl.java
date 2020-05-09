package com.ichzh.physicalFitness.service.impl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.model.Trend;
import com.ichzh.physicalFitness.repository.TrendRepository;
import com.ichzh.physicalFitness.service.TrendService;
import com.ichzh.physicalFitness.util.TrendComparator;

@Service("trendService")
public class TrendServiceImpl implements TrendService {

	@Autowired
	TrendRepository trendRepository;
	/**
	 * 查询某个学校的趋势数据.
	 * @param serviceBlock  学段 10002 小学  10003 初中
	 * @param schoolCode 学校代码
	 * @return
	 */
	public List<Trend> queryTrendBy(Integer serviceBlock, String schoolCode) {
		
		List<Trend> trends = trendRepository.findByServiceBlockAndSchoolCode(serviceBlock, schoolCode);
		if (trends != null && trends.size() > 0) {
			Collections.sort(trends, new TrendComparator());
		}
		return trends;
	}

	
}
