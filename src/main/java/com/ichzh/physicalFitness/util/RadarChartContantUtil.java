package com.ichzh.physicalFitness.util;

import lombok.extern.slf4j.Slf4j;

/**
 * 雷达图指标评价工具
 * @author yjf
 *
 */
@Slf4j
public class RadarChartContantUtil {

	//指标——学校发展
	public static int SCHOOL_DEVELOP = 1;
	
	//指标——生源情况
	public static int STUDENT_SOURCE_CASE = 2;
	
	//指标——师资力量Faculty strength
	public static int FACULTY_STRENGTH = 3;
	
	//指标——名师资源Famous teacher resources
	public static int FAMOUS_TEACHER_RESOURCE = 4;
	
	//指标——硬件条件Hardware condition
	public static int HARDWARE_CONDITION = 5;
	
	/**
	 * 根据指标类型和分值获取分值对应的解释
	 * @param quota           指标类型
	 * @param scoreValue      分值 
	 * @return
	 */
	public static String getEvaluationDesc(int quota, Float scoreValue) {
		String ret = "";
		if (quota == SCHOOL_DEVELOP) {
			ret = getSchoolDevEvaDesc(scoreValue);
		}else if (quota == STUDENT_SOURCE_CASE) {
			ret = getStudentSourceCaseEvaDesc(scoreValue);
		}else if (quota == FACULTY_STRENGTH) {
			ret = getFacultyStrengthEvaDesc(scoreValue);
		}else if (quota == FAMOUS_TEACHER_RESOURCE) {
			ret = getTeacherResourceEvaDesc(scoreValue);
		}else if (quota == HARDWARE_CONDITION) {
			ret = getHardConditionEvaDesc(scoreValue);
		}
		return ret;
	}
	
	/**
	 * 获得学校发展的评价描述
	 * @param scoreValue
	 * @return
	 */
	private static String getSchoolDevEvaDesc(Float scoreValue) {
		String ret = "";
		try
		{
			if (scoreValue.floatValue() <= Float.parseFloat("5")) {
				ret = "一般校，属于基础教育阶段绝大部分学校，为学生就近入学提供基础保障。";
			}else if (scoreValue.floatValue() <= Float.parseFloat("6") && scoreValue.floatValue() > Float.parseFloat("5")) {
				ret = "社会关注度一般，区域内热点校，学校发展潜力较大。";
			}else if (scoreValue.floatValue() <= Float.parseFloat("8") && scoreValue.floatValue() > Float.parseFloat("6")) {
				ret = "社会关注度较高，热点学校，学校发展潜力大。";
			}else if (scoreValue.floatValue() <= Float.parseFloat("10") && scoreValue.floatValue() > Float.parseFloat("8")) {
				ret = "社会关注度高，重点学校，学校发展得到社会认可。";
			}
		}catch(Exception ex) {
			log.warn(ex.getMessage(), ex);
		}
		return ret;
	}
	
	
	/**
	 * 获得生源情况的评价描述
	 * @param scoreValue
	 * @return
	 */
	private static String getStudentSourceCaseEvaDesc(Float scoreValue) {
		String ret = "";
		try
		{
			if (scoreValue.floatValue() <= Float.parseFloat("5") && scoreValue.floatValue() >= Float.parseFloat("0")) {
				ret = "学校片区内招生，符合入学条件的生源均可入学。";
			}else if (scoreValue.floatValue() <= Float.parseFloat("8") && scoreValue.floatValue() >= Float.parseFloat("6")) {
				ret = "学校区域位置较优越，可以接收相对优质的生源。";
			}else if (scoreValue.floatValue() <= Float.parseFloat("10") && scoreValue.floatValue() >= Float.parseFloat("9")) {
				ret = "学校生源情况优质，政策条件范围内，招收区域内较优质学生，同时学校优质升学率相对高。";
			}
		}catch(Exception ex) {
			log.warn(ex.getMessage(), ex);
		}
		return ret;
	}
	
	/**
	 * 获得师资力量的评价描述
	 * @param scoreValue
	 * @return
	 */
	private static String getFacultyStrengthEvaDesc(Float scoreValue) {
		String ret = "";
		try
		{
			if (scoreValue.floatValue() <= Float.parseFloat("3") && scoreValue.floatValue() >= Float.parseFloat("1")) {
				ret = "学校师资力量正常，可以保障日常教育教学工作开展。";
			}else if (scoreValue.floatValue() <= Float.parseFloat("6") && scoreValue.floatValue() >= Float.parseFloat("3")) {
				ret = "学校师资力量较强，可以充分保障日常教育教学工作高效开展。";
			}else if (scoreValue.floatValue() >= Float.parseFloat("6")) {
				ret = "学校师资力量雄厚，生均教师占比较多，学校教育教学水平较高。";
			}
		}catch(Exception ex) {
			log.warn(ex.getMessage(), ex);
		}
		return ret;
	}
	
	/**
	 * 获得名师资源的评价描述
	 * @param scoreValue
	 * @return
	 */
	private static String getTeacherResourceEvaDesc(Float scoreValue) {
		String ret = "";
		try
		{
			if (scoreValue.floatValue() <= Float.parseFloat("2")) {
				ret = "学校名师资源处于全市一般水平，可以保障日常教育教学工作开展。";
			}else if (scoreValue.floatValue() <= Float.parseFloat("6") && scoreValue.floatValue() >= Float.parseFloat("4")) {
				ret = "学校拥有名师资源，高级职称教师数量一般，学校教育教学工作处于前列。";
			}else if (scoreValue.floatValue() >= Float.parseFloat("8")) {
				ret = "学校拥有名师资源，高级职称教师较多，学校教育教学工作在全市有示范带头作用。";
			}
		}catch(Exception ex) {
			log.warn(ex.getMessage(), ex);
		}
		return ret;
	}
	
	/**
	 * 获得硬件条件的评价描述
	 * @param scoreValue
	 * @return
	 */
	private static String getHardConditionEvaDesc(Float scoreValue) {
		String ret = "";
		try
		{
			if (scoreValue.floatValue() < Float.parseFloat("4")) {
				ret = "学校硬件条件一般，绿化、场馆建设等各项指标生均占比较低。";
			}else if (scoreValue.floatValue() <= Float.parseFloat("6") && scoreValue.floatValue() >= Float.parseFloat("4")) {
				ret = "学校为集团化办学或拥有较大的占地面积、建筑面积，同时学校在绿化、场馆建设等硬件条件中处于生均占比处于领先水平。";
			}else if (scoreValue.floatValue() <= Float.parseFloat("10") && scoreValue.floatValue() >= Float.parseFloat("7")) {
				ret = "学校为集团化办学或拥有较大的占地面积、建筑面积，同时学校在绿化、场馆建设等硬件条件中处于全市领先水平。";
			}
		}catch(Exception ex) {
			log.warn(ex.getMessage(), ex);
		}
		return ret;
	}
}
