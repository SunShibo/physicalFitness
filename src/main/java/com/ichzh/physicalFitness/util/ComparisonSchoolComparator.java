package com.ichzh.physicalFitness.util;

import java.util.Comparator;

import com.ichzh.physicalFitness.model.ComparisonSchool;

public class ComparisonSchoolComparator implements Comparator<ComparisonSchool> {

	public int compare(ComparisonSchool o1, ComparisonSchool o2) {
		
		if (o1.getOrderNum().compareTo(o2.getOrderNum()) > 0){
			return 1;
		}
		if (o1.getOrderNum().compareTo(o2.getOrderNum()) < 0){
			return -1;
		}
		return 0;
	}
}
