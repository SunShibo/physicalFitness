package com.ichzh.physicalFitness.util;

import java.util.Comparator;

import com.ichzh.physicalFitness.model.EnrollmentSchedule;

public class EnrollmentScheduleComparator implements Comparator<EnrollmentSchedule>{

	//按年倒排，按日期升序排
   public int compare(EnrollmentSchedule o1, EnrollmentSchedule o2) {
		
	   int flag = o2.getYearYear().compareTo(o1.getYearYear());
	   if (flag == 0) {
		   return o1.getScheduleDate().compareTo(o2.getScheduleDate());
	   }else {
		   return flag;
	   }
	}
}
