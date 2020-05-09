package com.ichzh.physicalFitness.util;

import java.util.Comparator;

import com.ichzh.physicalFitness.model.EnrollmentScheduleList;

public class EnrollmentScheduleListComparator implements Comparator<EnrollmentScheduleList>{

	//按年倒排，按日期升序排
   public int compare(EnrollmentScheduleList o1, EnrollmentScheduleList o2) {
		
	   int flag = o2.getYearYear().compareTo(o1.getYearYear());
	   if (flag == 0) {
		   return o1.getScheduleDateBegin().compareTo(o2.getScheduleDateBegin());
	   }else {
		   return flag;
	   }
	}
}
