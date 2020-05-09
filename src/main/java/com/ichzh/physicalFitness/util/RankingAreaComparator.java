package com.ichzh.physicalFitness.util;

import java.util.Comparator;

import com.ichzh.physicalFitness.model.RankingArea;

public class RankingAreaComparator implements Comparator<RankingArea>{

//先按年倒排，再按片区升序排，再按排名升序排
public int compare(RankingArea o1, RankingArea o2) {
		
	    int flag = o2.getYearYear() - o1.getYearYear();
	    if (flag == 0) {
	    	int flag2 = o1.getAreaName().compareToIgnoreCase(o2.getAreaName());
	    	if (flag2 == 0) {
	    		return o1.getRanking() - o2.getRanking();
	    	}else {
	    		return flag2;
	    	}
	    }else {
	    	return flag;
	    }
	}
}
