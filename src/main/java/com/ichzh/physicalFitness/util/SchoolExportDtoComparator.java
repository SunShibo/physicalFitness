package com.ichzh.physicalFitness.util;

import java.util.Comparator;

import com.ichzh.physicalFitness.dto.SchoolExportDto;

public class SchoolExportDtoComparator implements Comparator<SchoolExportDto> {

	// 按照占比倒序排列
	public int compare(SchoolExportDto o1, SchoolExportDto o2) {
			
		if (o1.getSxRatio().compareTo(o2.getSxRatio()) > 0){
			return -1;
		}
		if (o1.getSxRatio().compareTo(o2.getSxRatio()) < 0){
			return 1;
		}
		return 0;
	}

	
}
