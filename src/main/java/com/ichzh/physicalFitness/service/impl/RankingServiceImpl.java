package com.ichzh.physicalFitness.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.model.Ranking;
import com.ichzh.physicalFitness.service.ICacheApplicationService;
import com.ichzh.physicalFitness.service.RankingService;

@Service("rankingService")
public class RankingServiceImpl implements RankingService{

	@Autowired
    ICacheApplicationService applicationService;
	
	/**
	 * 查询最新一年排名前5的学校及当前学校排名第几
	 * @param serviecBlock
	 * @param schoolCode
	 * @param town
	 * @return  key selfSort value Integer;  key  top5  value List<Ranking>
	 */
	public Map<String, Object> getTop5RankingAndSelfSortNum(Integer serviecBlock, String schoolCode, Integer town) {
		
		Map<String, Object> ret = new HashMap<String, Object>();
		//前五的学校
		List<Ranking> top5Ranking = new ArrayList<Ranking>();
		//当前学校的排名
		Integer selfSortNum = -1;
		
		List<Ranking> rankingSchoolOflatestYear = applicationService.getRankingSchoolOflatestYear(town);
		if (rankingSchoolOflatestYear != null  && rankingSchoolOflatestYear.size() > 0) {
			for (Ranking oneRanking :  rankingSchoolOflatestYear) {
				if (top5Ranking.size() < 5) {
					top5Ranking.add(oneRanking);
				}else if(selfSortNum > -1){
					break;
				}
				if (oneRanking.getServiceBlock().compareTo(serviecBlock) == 0 &&  oneRanking.getSchoolCode().equals(schoolCode)) {
					selfSortNum = oneRanking.getRanking();
					if (top5Ranking.size() >= 5) {
						break;
					}
				}
			}
		}
		
		ret.put("selfSort", selfSortNum);
		ret.put("top5", top5Ranking);
		
		return ret;
	}

	
	
}
