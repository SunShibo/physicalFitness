/**
 * 
 */
package com.ichzh.easy4enrolwas.util;
/**
 * @author smileyang
 *
 */
public class Constant {

	/**
	 * 默认密码
	 */
	public final static String DEFAULT_PASS = "123456";
	
	/**
	 * session存当前导航信息的key
	 */
	public final static String CURRENT_BREADCRUMB = "breadcrumb";
	
	public final static String INIT_ADMIN_USER_NAME = "admin@rdfz.cn";
	/**
	 * 角色—管理员
	 */
	public final static String ROLE_ADMIN = "admin";
	/**
	 * 角色—教师
	 */
	public final static String ROLE_TEACHER = "teacher";
	/**
	 * 角色—学生
	 */
	public final static String ROLE_STUDENT = "student";
	/**
	 * 角色—年级组长
	 */
	public final static String ROLE_LHEAD = "lhead";
	/**
	 * 角色—家长
	 */
	public final static String ROLE_GUARDIAN = "guardian";     
	/**
	 * 字典类型-学科
	 */
	public final static int SYSDICT_TYPE_SUBJECT = 100;
	/**
	 * API调用反馈消息代码定义—成功.
	 */
	public final static int OPT_RESULT_CODE_SUCCESS = 0;
	/**
	 * API调用反馈消息代码定义—失败.
	 */
	public final static int OPT_RESULT_CODE_FAIL = -1;
	/**
	 * API调用反馈消息代码定义—警告.
	 */
	public final static int OPT_RESULT_CODE_WARNING = -2;
	/**
	 * API调用反馈消息代码定义—登录超时.
	 */
	public final static int OPT_RESULT_CODE_SESSION_TIMEOUT = 1;
	/**
	 * 对试题序号的操作类型-对序号执行“加”操作.
	 */
	public final static String ORDER_OPTION_TYPE_PLUS = "plus";
	/**
	 * 对试题序号的操作类型-对序号执行“减”操作.
	 */
	public final static String ORDER_OPTION_TYPE_MINUS = "minus";
	/**
	 * 题类型—单选
	 */
	public final static Integer QUESTION_TYPE_DANXUAN = 120010;
	/**
	 * 题类型—多选
	 */
	public final static Integer QUESTION_TYPE_DUOXUAN = 120020;
	/**
	 * 题类型—对错
	 */
	public final static Integer QUESTION_TYPE_DUICUO = 120030;
	/**
	 * 题类型—单词填写
	 */
	public final static Integer QUESTION_TYPE_DANCITIANXIE = 120040;
	/**
	 * 题类型—填空
	 */
	public final static Integer QUESTION_TYPE_TIANKONG = 120050;
	/**
	 * 题类型—连线
	 */
	public final static Integer QUESTION_TYPE_LIANXIAN = 120060;
	/**
	 * 题类型—问答
	 */
	public final static Integer QUESTION_TYPE_WENDA = 120070;
	/**
	 * 题类型—附件提交
	 */
	public final static Integer QUESTION_TYPE_FUJIANTIJIAO = 120080;
	/**
	 * 任务类型-考试
	 */
	public final static Long ASSESSMENT_TYPE_EXAM = new Long(110050);
	/**
	 * session key 存当前语言
	 */
	public final static String KEY_CURRENT_LANG = "current_lang";
	
	public final static String KEY_CURRENT_ROLE = "currentRole";
	
	public final static String KEY_CURRENT_WARD = "currentWard";
	
}

