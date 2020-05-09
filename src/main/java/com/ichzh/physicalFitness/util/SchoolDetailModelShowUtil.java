package com.ichzh.physicalFitness.util;

import com.ichzh.physicalFitness.model.SchoolChoice;

/**
 * 控制学校详情中模块是否需要显示的计算工具
 * @author yjf
 *
 */
public class SchoolDetailModelShowUtil {

	//学校历史是否显示
	public static int schoolHistoryIfShow(SchoolChoice schoolChoice) {
		return 1;
	} 
	
	//师资情况是否显示
	public static int teacherCaseIfShow(SchoolChoice schoolChoice) {
		return 1;
	}
	
	//师资情况是否显示
	public static int studentCaseIfShow(SchoolChoice schoolChoice) {
		return 1;
	}
	
	//硬件条件是否显示
	public static int hardwareConditionIfShow(SchoolChoice schoolChoice) {
		return 1;
	}
}
