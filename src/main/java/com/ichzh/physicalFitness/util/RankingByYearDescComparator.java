package com.ichzh.physicalFitness.util;

import java.util.Comparator;

import com.ichzh.physicalFitness.model.Ranking;

public class RankingByYearDescComparator implements Comparator<Ranking>{

public int compare(Ranking o1, Ranking o2) {
		
		if (o1.getYearYear().compareTo(o2.getYearYear()) > 0){
			return -1;
		}
		if (o1.getYearYear().compareTo(o2.getYearYear()) < 0){
			return 1;
		}
		return 0;
	}
}
