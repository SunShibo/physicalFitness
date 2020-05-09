package com.ichzh.physicalFitness.util;

import java.util.Comparator;

import com.ichzh.physicalFitness.model.Trend;

public class TrendComparator  implements Comparator<Trend>{

	public int compare(Trend o1, Trend o2) {
		
		if (o1.getYearYear().compareTo(o2.getYearYear()) > 0){
			return 1;
		}
		if (o1.getYearYear().compareTo(o2.getYearYear()) < 0){
			return -1;
		}
		return 0;
	}
}
