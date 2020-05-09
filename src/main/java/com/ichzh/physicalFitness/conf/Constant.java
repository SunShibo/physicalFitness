package com.ichzh.physicalFitness.conf;

public class Constant {

	/**
	 * 用户类型：学校用户
	 */
	public  static int USER_TYPE_SCHOOL = 110010;
	/**
	 * 用户类型：区县用户
	 */
	public  static int USER_TYPE_TOWN = 110020;
	/**
	 * 用户类型：市级用户
	 */
	public  static int USER_TYPE_CITY = 110030;
	/**
	 * session中存验证码的key：demo验证码
	 */
	public static String SESSION_CHECK_CODE_FOR_DEMO = "sessionCheckcodeForDemo";
	/**
	 * 导出PDF时边框样式类别：第一行，上下左右无边框；第二行，只有下边框；最后一行只有上边框。
	 */
	public static int PDF_TEMPLATE_KIND_ONE = 4; 
	/**
	 *  导出PDF时边框样式类别：第一行，上下左右无边框；第二行 ，上下左右无边框； 第三行，只有下边框； 最后一行只有上边框。
	 */
	public static int PDF_TEMPLATE_KIND_TWO = 3; 
	/**
	 *  导出PDF时边框样式类别：每一行具有边框
	 */
	public static int PDF_TEMPLATE_KIND_DEFAULT = 2;

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
	
	/** 10701   入学依据—入学条件 */
	public final static int FUNCTION_CODE_10701 = 10701;
	/** 10702   入学依据—入学方式 */
	public final static int FUNCTION_CODE_10702 = 10702;
	/** 10703   入学依据—入学时间 */
	public final static int FUNCTION_CODE_10703 = 10703;
	/** 10704   入学依据—入学政策 */
	public final static int FUNCTION_CODE_10704 = 10704;
	/** 10705   入学匹配—居住地对应小学/中学 */
	public final static int FUNCTION_CODE_10705 = 10705;
	/** 10706   入学匹配—户籍地对应小学/中学 */
	public final static int FUNCTION_CODE_10706 = 10706;
	/** 10707   入学匹配—派位范围 */
	public final static int FUNCTION_CODE_10707 = 10707;
	/** 10708   入学匹配—自主招生 */
	public final static int FUNCTION_CODE_10708 = 10708;
	/** 10709   入学优选—招生范围*/
	public final static int FUNCTION_CODE_10709 = 10709;
	/** 10710   入学优选—到校查询 */
	public final static int FUNCTION_CODE_10710 = 10710;
	/** 10711   入学优选—分类查询(幼儿园) */
	public final static int FUNCTION_CODE_10711 = 10711;
	/** 10712   入学优选—定位附近的园(幼儿园) */
	public final static int FUNCTION_CODE_10712 = 10712;
	/** 10713   入学优选—学校优选(幼儿园) */
	public final static int FUNCTION_CODE_10713 = 10713;
	/** 10715   入学依据—入学日程 */
	public final static int FUNCTION_CODE_10715 = 10715;
	
	/** 服务模块	上幼儿园 */
	public final static int DICT_ID_10001 = 10001;
	/** 服务模块	上小学 */
	public final static int DICT_ID_10002 = 10002;
	/** 服务模块	上初中*/
	public final static int DICT_ID_10003 = 10003;
	/** 服务模块	中考中招 */
	public final static int DICT_ID_10004 = 10004;
	/** 服务模块	高考高招 */
	public final static int DICT_ID_10005 = 10005;
	/** 字典类型：初中等级*/
	public final static int DICT_TYPE_112 = 112;
	/**
	 * 无要求
	 */
	public final static int DICT_ID_10000 = 10000;
	/**
	 * 字典类型—户籍
	 */
	public final static int DICT_TYPE_104 = 104;
	/**
	 * 字典类型—居住地
	 */
	public final static int DICT_TYPE_105 = 105;
	/**
	 * 字典类型—学籍
	 */
	public final static int DICT_TYPE_103 = 103;
	/**
	 * API编号—居住地对学校查询
	 */
	public final static int DICT_ID_110005 = 110005;
	/**
	 * API编号—派位范围查询
	 */
	public final static int DICT_ID_110006 = 110006;
	
	/**
	 * API编号—招生范围查询
	 */
	public final static int DICT_ID_110008 = 110008;
	
	/**
	 * API编号—幼儿园学校详情查询
	 */
	public final static int DICT_ID_110009 = 110009;
	/**
	 * API编号—中小学学校详情查询
	 */
	public final static int DICT_ID_110010 = 110010;
	/**
	 * API编号—升学规划
	 */
	public final static int DICT_ID_110013 = 110013;
	/**
	 * API编号-招生范围查询结果——查询更多
	 */
	public final static int DICT_ID_110014 = 110014;
	/**
	 * API编号—虚拟，只用在每个人每个商品的免费次数表中.
	 */
	public final static int DICT_ID_110888 = 110888;
	/**
	 * 商品类型——会员
	 */
	public final static int DICT_ID_10901 = 10901;
	/**
	 * 商品类型——API
	 */
	public final static int DICT_ID_10902 = 10902;
	/**
	 * 会员商品的商品标识号
	 */
	public final static String COMMODITY_MEMBER_ID = "2e188ac18c2845ecb44bda619b3841ae";
	/**
	 * 分享成功添加学豆
	 */
	public final static int BEANS_SHARE_SUCCESS = 10;
	/**
	 * 购买vip成功添加学豆
	 */
	public final static int BEANS_BUY_VIP_SUCCESS = 47;
}
