/**
 * 
 */
package com.ichzh.physicalFitness.util;

import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.util.URIUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;



/**
 * @author smileYang
 *
 */
@Slf4j
public class CommonUtil {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(CommonUtil.class);
	private static final SimpleDateFormat sdfY = new SimpleDateFormat("yyyy");
	private static final SimpleDateFormat sdfM = new SimpleDateFormat("MM");
	private static final SimpleDateFormat sdfD = new SimpleDateFormat("dd");
	private static final SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 将一个字符串转化成对应的日期时间格式.
	 * @param dt
	 * @return
	 */
	public static Date StringToDateTime(String dt) {

		Date date = null;

		if (dt == null || dt.equals("") || dt.equals("null")) {
			return date;
		}

		if (dt != null && dt.length() > 0 && dt.length() < 11) {
			dt = dt.trim() + " 00:00:00";
		}
		DateFormat formatter = DateFormat.getDateTimeInstance(
				DateFormat.MEDIUM, DateFormat.MEDIUM, new Locale("zh", "CN"));

		try {
			date = formatter.parse(dt);

		} catch (java.text.ParseException e) {
			date = null;
		}
		return date;
	}

	/**
	 * 将一个字符串转化成对应的日期时间格式.
	 * 
	 * @param dt
	 *            字符串表示的日期.
	 * @return date.
	 * 
	 */
	public static Date stringToDateTime(String dt) {

		Date date = null;

		if (dt == null || dt.equals("") || dt.equals("null")) {
			return date;
		}

		if (dt != null && dt.length() > 0 && dt.length() < 11) {
			dt = dt.trim() + " 00:00:00";
		}
		DateFormat formatter = DateFormat.getDateTimeInstance(
				DateFormat.MEDIUM, DateFormat.MEDIUM, new Locale("zh", "CN"));

		try {
			date = formatter.parse(dt);

		} catch (java.text.ParseException ex) {
			date = null;
			logger.error("系统：exam;" + ex.getMessage() + ";"
					+ ex.fillInStackTrace());
		}
		return date;
	}

	/**
	 * 一次md5加密
	 * 
	 * @param primalPass
	 *            原始密码.
	 * @return String.
	 */
	public static String md5(String primalPass) {
		String ret = primalPass;
		try {
			Md5PasswordEncoder md5 = new Md5PasswordEncoder();
			ret = md5.encodePassword(primalPass, null);
		} catch (Exception ex) {
			logger.error(ex.getMessage() + ";" + ex.fillInStackTrace());
		}
		return ret;
	}
	//检测两个日期之间的间隔是多少天
	public static float  intervalBetweenTwoDate(Date date1, Date date2)
	{
		float  ret = 0;
		try
		{
			Calendar cal = Calendar.getInstance();

			cal.setTime(date1);
			long millisOfdate1 = cal.getTimeInMillis();

			cal.setTime(date2);
			long millisOfDate2 = cal.getTimeInMillis();

			float intervalOfTime = new Float(millisOfDate2 - millisOfdate1);
			float baseTime = new Float(3600 * 1000 * 24);

			ret = intervalOfTime/baseTime;

		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage() + ";" + ex.fillInStackTrace());
		}
		return ret;
	}
	   //检测两个时间之间的间隔是多少分钟
		public static int  intervalMinutesBetweenTwoDateTime(Date date1, Date date2)
		{
			float  ret = 0;
			try
			{
				Calendar cal = Calendar.getInstance();

				cal.setTime(date1);
				long millisOfdate1 = cal.getTimeInMillis();

				cal.setTime(date2);
				long millisOfDate2 = cal.getTimeInMillis();

				float intervalOfTime = new Float(millisOfDate2 - millisOfdate1);
				float baseTime = new Float(60 * 1000);

				ret = intervalOfTime/baseTime;
				return Integer.parseInt(round(Float.toString(ret), 0));
			}
			catch (Exception ex)
			{
				logger.error(ex.getMessage() + ";" + ex.fillInStackTrace());
				return 0;
			}
			
		}	
	// 根据出生日期得到年龄
	public static int getAge(Date birthday, Date now) {
		if (birthday == null || birthday.equals("") || birthday.equals("null")) {
			return 0;
		}
		return now.getYear() - birthday.getYear();
	}

	//从给定日期中取年
	public static int getYearForDate(Date examdate)
	{
		int ret = 0;
		try
		{
			Calendar cal = Calendar.getInstance();
			cal.setTime(examdate);
			return cal.get(Calendar.YEAR);
		}
		catch (Exception ex)
		{
			logger.error(ex.getMessage() + ";" + ex.fillInStackTrace());
		}
		return ret;
	}
	//当前时间（now）的几年前的时间
	public static Date dateBeforeYears(Date now, int years)
	{
		Date ret = now;
		try
		{
			Calendar cal = Calendar.getInstance();
			cal.setTime(now);

			cal.add(Calendar.YEAR, -years);

			ret = cal.getTime();

		}
		catch (Exception ex)
		{
			//不处理
		}
		return ret;
	}
	//获得由当前时间的年和月组成的字符串
	public static String getCurrentYearMonth(){
		String ret = "";
		try
		{
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());

			ret += cal.get(Calendar.YEAR);
			ret += cal.get(Calendar.MONTH)+1;
		}
		catch (Exception ex)
		{
			//不处理
		}
		return ret;
	}
	
	/**
	 * 获取年份
	 * @return
	 */
	public static String getCurrentYear(){
		return sdfY.format(new Date());
	}
	/**
	 * 获取月份
	 * @return
	 */
	public static String getCurrentMonth(){
		return sdfM.format(new Date());
	}
	/**
	 * 获取日期
	 * @return
	 */
	public static String getCurrentDay(){
		return sdfD.format(new Date());
	}
	
	/**
	 * 相对路径转绝对路径
	 * @param fileRelativePath  相对于classes目录的文件路径(含文件全名)
	 * @return String.
	 * @throws IOException
	 */
	public static String getAbsolutePath(String fileRelativePath) throws IOException
	{
		String ret = "";
		ClassPathResource classPathResource = new ClassPathResource(fileRelativePath);
		try
		{
			ret = classPathResource.getURL().getPath();
		}
		catch (IOException ioe)
		{
			throw ioe;
		}

		return ret;
	}
	/**
	 * 相对路径转绝对路径
	 * @param sc   ServletContext.         
	 * @param fileRelativePath  相对于web应用的根目录的文件路径(含文件全名)
	 * @return  String.
	 * @throws IOException
	 */
	public static String getAbsolutePath(
			ServletContext sc, String fileRelativePath) throws IOException
			{
		String ret = "";
		ApplicationContext ctx = WebApplicationContextUtils.
		getWebApplicationContext(sc);
		Resource resource = ctx.getResource(fileRelativePath);
		try
		{
			ret = resource.getFile().getAbsolutePath();
		}
		catch (IOException ioe)
		{
			throw ioe;
		}
		return ret;
			}
	//获得文件的扩展名
	public static String getExtName(String fileName)
	{
		String ret = "";
		try
		{
			
			if (!StringUtils.isEmpty(fileName))
			{
				int position = fileName.lastIndexOf(".");
				if (position >= 0)
				{
					ret = fileName.substring(position + 1);
				}
			}
		}
		catch (Exception ex)
		{
			//不处理
		}
		return ret;
	}

	/**
	 * 提供精确的加法运算。
	 * @param v1 被加数
	 * @param v2 加数
	 * @return 两个参数的和
	 */
	public static BigDecimal add(String v1,String v2){
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.add(b2);
	}
	/**
	 * 提供精确的乘法运算。
	 * @param v1 被乘数
	 * @param v2 乘数
	 * @return 两个参数的积
	 */
	public static BigDecimal mul(String v1,String v2){
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2);
	}
	/** 
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 
	 * 定精度，以后的数字四舍五入。 
	 * @param v1 被除数 
	 * @param v2 除数 
	 * @param scale 表示表示需要精确到小数点以后几位。 
	 * @return 两个参数的商 
	 */ 
	public static String div(String v1, String v2, int scale) { 
		if (scale < 0) { 
			throw new IllegalArgumentException("The scale must be a positive integer or zero"); 
		} 
		BigDecimal a = new BigDecimal(v1); 
		if(a.intValue()==0){
			return "";
		}
		BigDecimal b = new BigDecimal(v2); 
		if(b.intValue()==0){
			return "";
		}
		return a.divide(b, scale, BigDecimal.ROUND_HALF_UP).toString(); 
	} 
	/** 
	 * 提供精确的小数位四舍五入处理(>=.5就进一，如：2.55，保留一位小数，则该方法返回2.6；2.56，保留一位小数，则该方法返回2.6)。  
	 * @param v 需要四舍五入的数字 
	 * @param scale 小数点后保留几位 
	 * @return 四舍五入后的结果 
	 */ 
	public static String round(String v, int scale) { 
		if (scale < 0) { 
			throw new IllegalArgumentException( 
			"The scale must be a positive integer or zero"); 
		} 
		BigDecimal b = new BigDecimal(v); 
		return b.setScale(scale, RoundingMode.HALF_UP).toString(); 
	} 
	/** 
	 * 提供精确的小数位非四舍五入处理(>.5才进一，如：2.55，保留一位小数，则该方法返回2.5；2.56，保留一位小数，则该方法返回2.6)。 
	 * @param v 需要四舍五入的数字 
	 * @param scale 小数点后保留几位 
	 * @return 四舍五入后的结果 
	 */ 
	public static String roundMoreThan5(String v, int scale) { 
		if (scale < 0) { 
			throw new IllegalArgumentException( 
			"The scale must be a positive integer or zero"); 
		} 
		BigDecimal b = new BigDecimal(v); 
		return b.setScale(scale, RoundingMode.HALF_DOWN).toString(); 
	} 
	/**
	 * 两个整数相除，向上取整 如：相除的结果为1.2  返回的值为2
	 * @param a1
	 * @param a2
	 * @return
	 */
	public static int divOfUpGetInteger(int a1, int a2) {
		//取整
		int integerPart = a1 / a2;
		//余数部分
		int remainderPart = a1 %  a2;
		if (remainderPart > 0) {
			return integerPart + 1;
		}else {
			return integerPart;
		}
	}
	/**
	 * 有效期从数字转换成字符串
	 * @param applyValidate
	 * @return
	 */
	public static String intToStrApplyValidate(Integer applyValidate)
	{
		String strApplyValidate = null;
		if(applyValidate == 1)
		{
			strApplyValidate = "半个月";
		}
		if(applyValidate == 2)
		{
			strApplyValidate = "一个月";
		}
		if(applyValidate == 3)
		{
			strApplyValidate = "两个月";
		}
		return strApplyValidate;
	}
	/**
	 * 有效期从字符串转换成数字
	 * @param applyValidate
	 * @return
	 */
	public static int strToIntApplyValidate(String applyValidate)
	{
		Integer intApolyValidate = null;
		if(applyValidate.equals("半个月"))
		{
			intApolyValidate = 1;
		}
		if(applyValidate.equals("一个月"))
		{
			intApolyValidate = 2;
		}
		if(applyValidate.equals("两个月"))
		{
			intApolyValidate = 3;
		}
		return intApolyValidate;
	}
	// 生成随即的指定位数的字符串
	public static String createRandomPass(int bitNum) {
		return RandomStringUtils.random(bitNum, "abcdefghjkmnpqrst");
	}
	//判断字符是否为汉字
	public static boolean ifChinese(char targetChar)
	{
		boolean ret = false;
		try
		{
			Matcher mathcer = Pattern.compile("[\u4e00-\u9fa5]").matcher(targetChar+"");
			if (mathcer.find())
			{
				ret = true;
			}
		}
		catch(Exception ex)
		{
			//不处理
		}
		return ret;
	}
	//判断字符是否为字母或数字
	public static boolean ifEnglishWordOrNumber(char targetChar)
	{
		boolean ret = false;
		try
		{
			Matcher mathcer = Pattern.compile("[0123456789a-zA-Z]").matcher(targetChar+"");
			if (mathcer.find())
			{
				ret = true;
			}
		}
		catch(Exception ex)
		{
			//不处理
		}
		return ret;
	}

	/**
	 * 日期月加addMonth
	 * @param date
	 * @param addMonth
	 * @return
	 */
	public static Date addMonth(Date date,int addMonth,int setDate){
		Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		instance.add(Calendar.MONTH, addMonth);
		instance.set(Calendar.DATE, setDate);
		return instance.getTime();
	}
	/**
	 * 日期天加addDate
	 * @param date
	 * @param addDate
	 * @return
	 */
	public static Date addDate(Date date,int addDate){
		Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		instance.add(Calendar.DATE, addDate);
		return instance.getTime();
	}

	/**
	 * 获取一天开始时间
	 * @return
	 */
	public static Date getDateStart(Date date){
		Calendar ca=Calendar.getInstance();
		ca.setTime(date);
		ca.set(Calendar.HOUR,0);
		ca.set(Calendar.MINUTE,0);
		ca.set(Calendar.SECOND,0);
		return ca.getTime();
	}
	/**
	 * 判定图片宽高
	 * @param 
	 * @param fileWidth
	 * @param fileHeight
	 * @return
	 */
	public static boolean ifLicitumImg(FileItem fileItem,String fileWidth,String fileHeight){
		try {
			BufferedImage image = ImageIO.read(fileItem.getInputStream());
			int width = image.getWidth();
			int height = image.getHeight();
			BigDecimal sysWh = mul(String.valueOf(width), String.valueOf(height));
			BigDecimal noSysWh = mul(String.valueOf(fileWidth), String.valueOf(fileHeight));
			if(sysWh.compareTo(noSysWh)==1){
				return false;
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	/**
	 * 将某个数字字符串补足给定的位数.  如targetStr="1", 如果bitNum=4, 则返回0001
	 * @param targetStr
	 * @param bitNum
	 * @return String.
	 */
	public static String fillZero(String targetStr, int bitNum)
	{
		String ret = targetStr;
		try
		{
			//字符串的长度
			int strLen = targetStr.length();
			if (strLen < bitNum)
			{
				for (int i = 0; i < (bitNum - strLen); i++)
				{
					ret = "0"+ret;
				}
			}
			return ret;
		}
		catch (Exception ex)
		{
			return targetStr;
		}
	}
	/**
	 * 将给定的字符串用给定的分割符分割后返回Long集合
	 * @param targetStr
	 * @return
	 */
	public static List<Long> stringToList(String targetStr, String splitStr)
	{
		List<Long> ret = new ArrayList<Long>();
		try
		{
			String[] tempArr = targetStr.split(splitStr);
			for (String oneTempArrObj : tempArr)
			{
				if (!StringUtils.isEmpty(oneTempArrObj))
				{
					ret.add(new Long(oneTempArrObj));
				}
			}
		}
		catch(Exception ex)
		{
			logger.warn(ex.getMessage() + ex.fillInStackTrace());
		}
		return ret;
	}
	
	public static JavaType getCollectionType(ObjectMapper mapper, Class<?> collectionClass, Class<?>... elementClasses) {   
	       return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);   
	}   
	/**
	 * 将unicode字符串转汉字
	 * @param unicodeString
	 * @return
	 */
    public static String convert(String unicodeString){  
        StringBuilder sb = new StringBuilder();  
        int i = -1;  
        int pos = 0;  
          
        while((i=unicodeString.indexOf("\\u", pos)) != -1){  
            sb.append(unicodeString.substring(pos, i));  
            if(i+5 < unicodeString.length()){  
                pos = i+6;  
                sb.append((char)Integer.parseInt(unicodeString.substring(i+2, i+6), 16));  
            }  
        }  
          
        return sb.toString();  
    }  
    
    public static String timeLength2HourMinuteSecond(long milliSecond){
    	String ret = "00:00:00";
    	try
    	{
    		//小时数
    		long hour = 0;
    		//分钟数
    		long minute = 0;
    		//秒数
    		long second = 0;
    		
    		hour = milliSecond / 3600000;
    		long minute2 = milliSecond / 60000;
    		minute = minute2 % 60;
    		long second2 = milliSecond / 1000;
    		second = second2 % 60;
    		
    		ret = fillZero(hour+"", 2);
    		ret += ":" + fillZero(minute+"", 2);
    		ret += ":" + fillZero(second+"", 2);
    	}
    	catch(Exception ex)
    	{
    		logger.error("CommonUtil.timeLength2HourMinuteSecond。"+ex.getMessage() + ex.fillInStackTrace());
    	}
    	return ret;
    }
    /*
     * 执行批处理文件，导出的文件放在与批处理文件相同的目录下
     * batFileName : 批处理文件的全路径
     */
    public static boolean execBatFile(String batFileName){
    	try
    	{
    		Process ps = Runtime.getRuntime().exec(batFileName);
    		InputStream in = ps.getInputStream();
    		int c;
    		while((c=in.read()) != -1){
    		}
    		in.close();
			ps.waitFor();
			return true;
    	}
    	catch(IOException ioe){
    		logger.error(ioe.getMessage() + ioe.fillInStackTrace());
    		return false;
    	}
    	catch(InterruptedException e){
    		logger.error(e.getMessage() + e.fillInStackTrace());
    		return false;
    	}
    }
    
    public static boolean deleteSingleFile(String fileRelativePath, ServletContext servletContext){
    	boolean ret = false;
    	try
    	{
    		String fileAbsolutePath = servletContext.getRealPath(fileRelativePath);
    		File file = new File(fileAbsolutePath);
    		if(!file.delete()){
    			logger.warn("file delete fail:"+fileAbsolutePath);
    		}
    	}
    	catch(Exception ex)
    	{
    		logger.warn(ex.getMessage() + ex.fillInStackTrace());
    	}
    	return ret;
    }
    /**
     * 递归查询[[]]的内容, 并将分析结果通过第一个参数返回.
     * 例如：当targetStr为Jack and jill went up the [[hill]] to fetch a pail of [[water]]
     * 查询结果为：list[hill,water] 
     * @return
     */
    public static void recursionQueryGroup(List<String> items, String targetStr){
    	try
    	{
    		//查找第一个[[，第一个]]
    		int positonLeft = targetStr.indexOf("[[");
    		int postionRight = targetStr.indexOf("]]");
    		
    		if (positonLeft >=0 && postionRight >= 0 && postionRight > positonLeft){
    			items.add(targetStr.substring(positonLeft+2, postionRight));
    			targetStr = targetStr.substring(postionRight+2);
    			recursionQueryGroup(items, targetStr);
    		}
    		else
    		{
    			return;
    		}
    	}
    	catch(Exception ex)
    	{
    		logger.error(ex.getMessage() + ex.fillInStackTrace());
    	}
    }
    /** 
     * 复制整个文件夹的内容(含自身) 
     * @param oldPath 准备拷贝的目录 
     * @param newPath 指定绝对路径的新目录 
     * @return 
     */  
    public static void copyFolderWithSelf(String oldPath, String newPath) {  
        try {  
        	File newFilePath = new File(newPath);
        	if (!newFilePath.exists()){
        		//如果文件夹不存在 则建立新文件夹  
        		if(!newFilePath.mkdirs()){
        			//创建文件失败
        			logger.error("创建文件失败:"+newPath);
        			return;
        		}
        	}
            File dir = new File(oldPath);  
            // 目标  
            newPath +=  File.separator + dir.getName();  
            File moveDir = new File(newPath);  
            if(dir.isDirectory()){  
                if (!moveDir.exists()) {  
                    if(!moveDir.mkdirs()){
                    	//创建文件失败
            			logger.error("创建文件失败:"+newPath);
            			return;
                    }  
                }  
            }  
            String[] file = dir.list();  
            File temp = null;  
            for (int i = 0; i < file.length; i++) {  
                if (oldPath.endsWith(File.separator)) {  
                    temp = new File(oldPath + file[i]);  
                } else {  
                    temp = new File(oldPath + File.separator + file[i]);  
                }  
                if (temp.isFile()) {  
                    FileInputStream input = new FileInputStream(temp);  
                    FileOutputStream output = new FileOutputStream(newPath +  
                    		File.separator +  
                            (temp.getName()).toString());  
                    byte[] b = new byte[1024 * 5];  
                    int len;  
                    while ((len = input.read(b)) != -1) {  
                        output.write(b, 0, len);  
                    }  
                    output.flush();  
                    output.close();  
                    input.close();  
                }  
                if (temp.isDirectory()) { //如果是子文件夹  
                    copyFolderWithSelf(oldPath + File.separator + file[i], newPath);  
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }     
    /**
     * 时间转换为字符串
     * @param date
     * @return yyyy-MM-dd HH:mm:ss格式的字符串
     */
    public static  String DateTimeToString(Date date){
    	String ret = "";
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
		     if(date!=null){
		    	 ret=sdf.format(date);
		     }
		} catch (Exception ex) {
			logger.error(ex.getMessage() + ";" + ex.fillInStackTrace());
		}
		return ret;
    }
    
    /**
     * 日期转换为字符串
     * @param date
     * @return yyyy-MM-dd格式的字符串
     */
    public static  String DateToDateString(Date date){
    	String ret = "";
    	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		try {
		     if(date!=null){
		    	 ret=sdf.format(date);
		     }
		} catch (Exception ex) {
			logger.error(ex.getMessage() + ";" + ex.fillInStackTrace());
		}
		return ret;
    }
    /**
     * 时间戳转换成时间
     * @param cc_time
     * @return
     */
    public static String getStrTime(String cc_time) {  
    String re_StrTime = null;      
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
    long lcc_time = Long.valueOf(cc_time);  
    re_StrTime = sdf.format(new Date(lcc_time * 1000L));        
    return re_StrTime;      
    }  
    /**
     * 
     * 得到UUID
     * @return
     */
    public static  String getUUID(){
    	return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }
    
    /**
     * 根据长度获取随机串-获取salt
     * @param length 长度
     * @return 随机字母串
     */
    public static String getSalt(int length){
    	String[] stringArry=new String[]{"a","b","c","d","e","f","g","h","i","j","k",
    			"l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
    	String salt="";
    	Random random = new Random();
    	if(length > 0){
    		for(int i=1;i<=length;i++){
    			int index=random.nextInt(25);
    			salt+=stringArry[index];
    		}
    	}
    	return salt;
    }
    /**
     * 得到Icon路径
     * @param path
     * @return
     */
    public static String getIcoPath(String path){
        if(StringUtils.isEmpty(path))
        	return "";
        StringBuilder sb=new StringBuilder();
       int index = path.lastIndexOf(".");
        if(index>0)
        return sb.append(path.substring(0,index)).append("_ico").append(path.substring(index)).toString();
        else return "";
    }
    /**
     * 生成num位字符
     * @param num
     * @return
     */
    public static char[] generateRandomArray(int num) {  
        String chars = "0123456789abcdefghigklmnopqrstuvwxyz";  
        char[] rands = new char[num];  
        for (int i = 0; i < num; i++) {  
            int rand = (int) (Math.random() * 36);  
            rands[i] = chars.charAt(rand);  
        }  
        return rands;  
    }  
    /**
     * 获取当前时间几天之后的日期字符串
     * @param days
     * @return
     */
    public static String getLaterDateStr(int days){
    	Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTime(new Date());
		tomorrow.add(Calendar.DATE,days);
		Date tomorDate= tomorrow.getTime();
		String tomorStr = new SimpleDateFormat("yyyy-MM-dd").format(tomorDate);
		return tomorStr;
    }
    
    /**
     * 获取当前时间几天之前的日期字符串
     * @param days
     * @return
     */
    public static String getBeforeDateStr(int days){
    	Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTime(new Date());
		tomorrow.add(Calendar.DATE,-days);
		Date tomorDate= tomorrow.getTime();
		String tomorStr = new SimpleDateFormat("yyyy-MM-dd").format(tomorDate);
		return tomorStr;
    }
    
    /**
     * 获取当前时间几个工作日之后的日期字符串（没去掉串休）
     * @param days
     * @return
     */
    public static String getLaterWorkDateStr(int days){
    	Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTime(new Date());
		//如果是周末不执行
		for(int i = 0;i<=days;i++){
			do{
				tomorrow.add(Calendar.DATE,1);
			}while(tomorrow.get(Calendar.DAY_OF_WEEK)-1 == 0 || tomorrow.get(Calendar.DAY_OF_WEEK)-1 == 6);
		}
		Date tomorDate= tomorrow.getTime();
		String tomorStr = new SimpleDateFormat("yyyy-MM-dd").format(tomorDate);
		return tomorStr;
    }

    /**
     * 根据某个日期获取给定的当前周的周几
     * @param date
     * Calendar.MONDAY
     * @return
     */
    public static String getNowWeekDay(Date date,Integer day) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	cal.add(Calendar.DAY_OF_MONTH, -1);//解决周日会出现 并到下一周的情况    
    	if(day == 1){
    		cal.add(Calendar.WEEK_OF_YEAR, 1);
    	}
        cal.set(Calendar.DAY_OF_WEEK, day);
        return sdfYMD.format(cal.getTime());
    }

    public static String getDateNow(){
    	Calendar cal=Calendar.getInstance();

    	return sdfYMD.format(cal.getTime());
	}
    //得到一周的第几天是哪一天 周日为第一天（day=0），周六是最后一天(day=6)
    public static Date getDateOfCurrentWeek(int day) {
    	Calendar cal = Calendar.getInstance();
    	int day_of_week = cal.get(Calendar.DAY_OF_WEEK)-1;
    	cal.add(Calendar.DATE,-day_of_week+day);
    	return cal.getTime();
    }
    
    // 从周日、周一、周二......周六 分别对应哪天
    public static List<Date> getDayOfCurrentWeek(){
    	List<Date> ret = new ArrayList<Date>();
    	for (int i = 0; i <= 6; i++) {
        	ret.add(getDateOfCurrentWeek(i));
    	}
    	return ret;
    }
    
    //获得上一周是哪几天  beforeWeekNum -1 表示上一周  1 表示下一周
    public static List<Date> getDayOfBeforeOrAfterWeek(int beforeWeekNum){
    	List<Date> ret = new ArrayList<Date>();
    	List<Date> currentWeekDays = getDayOfCurrentWeek();
    	for (Date oneDate : currentWeekDays) {
    		ret.add(addDate(oneDate, beforeWeekNum * 7));
    		//System.out.println(sdfYMD.format(addDate(oneDate, beforeWeekNum * 7)));
    	}
    	return ret;
    }
    
    //获得上一周是哪几天  beforeWeekNum -1 表示上一周  1 表示下一周
    public static List<String> getDayOfBeforeOrAfterWeek2(int beforeWeekNum){
    	List<String> ret = new ArrayList<String>();
    	List<Date> currentWeekDays = getDayOfCurrentWeek();
    	for (Date oneDate : currentWeekDays) {
    		ret.add(sdfYMD.format(addDate(oneDate, beforeWeekNum * 7)));
    		System.out.println(sdfYMD.format(addDate(oneDate, beforeWeekNum * 7)));
    	}
    	return ret;
    }
    
    // 某一天是哪个月
    public static int getMonthOfSomeoneDay(Date date) {
    	Calendar cal = Calendar.getInstance();
    	cal.setTime(date);
    	int month = cal.get(Calendar.MONTH)+1;
    	return month;
    }

	/**
	 * 获取指定时间几个工作日之后的日期字符串（没去掉串休）  间隔 days
	 * @param days
	 * @return
	 */
	public static String getLaterWorkDateStr(Date date,int days){
		Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTime(date);
		//如果是周末不执行
		for(int i = 0;i<=days;i++){
			do{
				tomorrow.add(Calendar.DATE,1);
			}while(tomorrow.get(Calendar.DAY_OF_WEEK)-1 == 0 || tomorrow.get(Calendar.DAY_OF_WEEK)-1 == 6);
		}
		Date tomorDate= tomorrow.getTime();
		String tomorStr = new SimpleDateFormat("yyyy-MM-dd").format(tomorDate);
		return tomorStr;
	}
    
    /**
     * 获取当前时间几个工作日之前的日期字符串(没去掉串休) 间隔days
     * @param days
     * @return
     */
    public static String getBeforeWorkDateStr(int days){
    	Calendar tomorrow = Calendar.getInstance();
		tomorrow.setTime(new Date());
		//如果是周末不执行
		for(int i = 0;i<=days;i++){
			do{
				tomorrow.add(Calendar.DATE,-1);
			}while(tomorrow.get(Calendar.DAY_OF_WEEK)-1 == 0 || tomorrow.get(Calendar.DAY_OF_WEEK)-1 == 6);
		}
		Date tomorDate= tomorrow.getTime();
		String tomorStr = new SimpleDateFormat("yyyy-MM-dd").format(tomorDate);
		return tomorStr;
    }
    
    public static String getWeek(Date date){
		String[] weeks = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if(week_index<0){
			week_index = 0;
		} 
		return weeks[week_index];
	}
    
    /**
     * 获得所给日期的汉字月份
     * @param date
     * @return
     */
    public static String getMonth(Date date) {
    	String[] months = {"一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"};
    	Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int month_index = cal.get(Calendar.MONTH);
		return months[month_index];
    }
    
    public static String getChineseDateString(Date date) {
    	Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH)+1;
		int day = cal.get(Calendar.DAY_OF_MONTH);
		
		return year+"年"+month+"月"+day+"日";
    }
    
    /**
     * 模拟发送GET请求
     * @param url
     * @param queryString
     * @param charset
     * @param pretty
     * @return
     */
	public static String doGet(String url, String queryString, String charset,boolean pretty) {
		StringBuffer response = new StringBuffer();
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(url);
		try {
			if (queryString != null && !"".equals(queryString))// 对get请求参数做了http请求默认编码，好像没有任何问题，汉字编码后，就成为%式样的字符串
				method.setQueryString(URIUtil.encodeQuery(queryString));
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader( new InputStreamReader(method.getResponseBodyAsStream(), charset));
				String line;
				while ((line = reader.readLine()) != null) {
					if (pretty)
						response.append(line).append( System.getProperty("line.separator"));
					else
						response.append(line);
				}
				reader.close();
			}
		} catch (URIException e) {
			// log.error("执行HTTP Get请求时，编码查询字符串“" + queryString + "”发生异常！", e);
			e.printStackTrace();
		} catch (IOException e) {
			// log.error("执行HTTP Get请求" + url + "时，发生异常！", e);
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
		return response.toString();
	}
	/**
	 * 模拟发送POST请求
	 * @param url
	 * @param params
	 * @param charset
	 * @param pretty
	 * @return
	 */
	public static String doPost(String url, Map<String, String> params,
			String charset, boolean pretty) {
		StringBuffer response = new StringBuffer();
		HttpClient client = new HttpClient();
		HttpMethod method = new PostMethod(url);
		// 设置Http Post数据
		if (params != null) {
			HttpMethodParams p = new HttpMethodParams();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				p.setParameter(entry.getKey(), entry.getValue());
			}
			method.setParams(p);
		}
		try {
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(method.getResponseBodyAsStream(),
								charset));
				String line;
				while ((line = reader.readLine()) != null) {
					if (pretty)
						response.append(line).append(
								System.getProperty("line.separator"));
					else
						response.append(line);
				}
				reader.close();
			}
		} catch (IOException e) {
			// log.error("执行HTTP Post请求" + url + "时，发生异常！", e);
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
		return response.toString();
	}

	/**
	 * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址。
	 * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
	 * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串
	 * @param request
	 * @return
	 */
	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			if("127.0.0.1".equals(ip)||"0:0:0:0:0:0:0:1".equals(ip)){
				//根据网卡取本机配置的IP
				InetAddress inet=null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ip= inet.getHostAddress();
			}
		}
		return ip;
	}
	
	public static String getServerIp(HttpServletRequest request){
		return request.getLocalAddr();
	}

	/**
	 * 获取本机IP地址
	 * @return
	 */
	public static String getLocalIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			log.error("获取本机IP地址失败", e);
		}
		return null;
	}

	public static String formatNumber3(String number){
        Assert.notNull(number,"number 不能为null");
        DecimalFormat df = new DecimalFormat("#,##0.##");
        return df.format(new BigDecimal(formatNumber2(number)));

    }
    
    public static String formatNumber2(String number){

        Assert.notNull(number,"number 不能为null");
        try {
            Double d = Double.valueOf(number);
            BigDecimal df   = new BigDecimal(d);
            Double r = df.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
            return r%1==0?(r.intValue()+""):(r+"");
        } catch (NumberFormatException e) {
            return null;
        }

    }

	/**
	 * 流转换为byte[] 数组
	 * @param inputStream 输入流
	 * @return
	 */
	public static byte[] getByteByInputStream(InputStream inputStream) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] imgData = new byte[1024];
		while (true) {
			int read = inputStream.read(imgData);
			if (read == -1) {
				break;
			}
			output.write(imgData, 0, read);
		}
		byte[] bytes = output.toByteArray();
		output.close();
		return bytes;
	}

	/**
	 * 获取文件的二进制内容 byte[]
	 * @param relativePath 项目编译后的以根目录为基准的相对路径，需以“/”开头
	 * @return
	 * @throws IOException
	 */
	public static byte[] getResourceByte(String relativePath) throws IOException {
		InputStream inputStream = CommonUtil.class.getResource(relativePath).openStream();
		return getByteByInputStream(inputStream);
	}

	/**
	 * 将文件从jar包中拷贝到jar包所在的目录,并返回文件的路径
	 * @param inputStream 文件流
	 * @param fileName 文件名
	 * @return
	 * @throws IOException
	 */
	public static String copyFileToRoot(InputStream inputStream, String fileName) throws IOException {
		// 项目所在的目录
		String filePath = JarPathUtil.getRootPath() + fileName;
		// 将流转为文件，放到项目的同级目录下
		FileCopyUtils.copy(inputStream, new FileOutputStream(filePath));
		return filePath;
	}

	/**
	 * 关流：输入流
	 * @param inputStreams
	 */
	public static void cloneInputStream(InputStream... inputStreams) {
		for (InputStream inputStream :  inputStreams) {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {
					// 关流异常处理
				}
			}
		}
	}

	/**
	 * 关流：输出流
	 * @param outputStreams
	 */
	public static void cloneOutputStream(OutputStream... outputStreams) {
		for (OutputStream outputStream :  outputStreams) {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (Exception e) {
					// 关流异常不处理
				}
			}
		}
	}
	
	  public static void main(String[] args) {
		   System.out.println(getChineseDateString(stringToDateTime("2019-04-07")));
	    }
}

