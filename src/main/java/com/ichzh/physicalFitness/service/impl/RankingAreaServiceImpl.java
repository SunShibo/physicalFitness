package com.ichzh.physicalFitness.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.model.Ranking;
import com.ichzh.physicalFitness.model.RankingArea;
import com.ichzh.physicalFitness.model.SchoolArea;
import com.ichzh.physicalFitness.service.ICacheApplicationService;
import com.ichzh.physicalFitness.service.RankingAreaService;

@Service("rankingAreaService")
public class RankingAreaServiceImpl implements RankingAreaService {

	@Autowired
    ICacheApplicationService applicationService;
	/**
	 * 查询最新一年排名前5的学校及当前学校排名第几
	 * @param serviecBlock
	 * @param schoolCode
	 * @return  key selfSort value Integer;  key  top5  value List<Ranking>
	 */
	public Map<String, Object> getTop5RankingAndSelfSortNum(Integer serviecBlock, String schoolCode) {
		
		Map<String, Object> ret = new HashMap<String, Object>();
		//前五的学校
		List<RankingArea> top5Ranking = new ArrayList<RankingArea>();
		//当前学校的排名
		Integer selfSortNum = -1;
		//查询当前学校所在的片区
		SchoolArea schoolArea = applicationService.getSchoolAreaBy(serviecBlock, schoolCode);
		if (schoolArea != null) {
			List<RankingArea> rankingSchoolOflatestYear = applicationService.getRankingAreaSchoolOflatestYear(schoolArea.getAreaName());
			if (rankingSchoolOflatestYear != null  && rankingSchoolOflatestYear.size() > 0) {
				for (RankingArea oneRanking :  rankingSchoolOflatestYear) {
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
		}
		
		
		return ret;
	}

	
}
