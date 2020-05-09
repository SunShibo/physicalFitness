package com.ichzh.physicalFitness.util;

import java.util.Comparator;

import com.ichzh.physicalFitness.model.Ranking;

public class RankingComparator implements Comparator<Ranking>{

//先按年倒排，再按区升序排，再按排名升序排
public int compare(Ranking o1, Ranking o2) {
		
	    int flag = o2.getYearYear() - o1.getYearYear();
	    if (flag == 0) {
	    	int flag2 = o1.getTown() - o2.getTown();
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
