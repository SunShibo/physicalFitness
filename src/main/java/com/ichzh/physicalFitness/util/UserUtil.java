package com.ichzh.physicalFitness.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 用户信息的工具类
 * @author xqx
 *
 */
public class UserUtil {
//	private static AppManagerRepository appManagerRepository;
//	private static DmUserRepository dmUserRepository;
	
	private static final String url="http://211.153.78.231/Api";
	private static final String words="sxcz_jsq_@)!%";
	private static final String company="sxcz";

	
	/**
	 * 通过eduId获取学生的手机号
	 * */
	public static String getStudentPhone(String eduId,String type){
//		DmUser stu = dmUserRepository.findByEducationId(eduId);
//		Integer groupId = 4;
		if(type.equals("3")){
			Map<String,Object> result = getStuInfo(eduId);
			if(result !=null && result.get("telephone")!=null&&!result.get("telephone").equals("")){
				return result.get("telephone")==null?"":result.get("telephone").toString();
			}else{
				return "";
			}
		}else{
			String timestamp = (new Date().getTime()/1000)+"";//获取时间戳
			String key = CommonUtil.md5(words+timestamp);//生成key
			String paramStrEncode=null;
			try {
				//按照接口要求的固定格式拼接encode后的param1=&param2=&
				paramStrEncode=URLEncoder.encode("args[company]", "utf-8")+"="+URLEncoder.encode(company, "utf-8")+"&"
						+URLEncoder.encode("args[key]", "utf-8")+"="+key+"&"+URLEncoder.encode("args[time]", "utf-8")
						+"="+URLEncoder.encode(timestamp, "utf-8")+"&"+URLEncoder.encode("args[type]", "utf-8")+"="+URLEncoder.encode(type, "utf-8")
						+"&"+URLEncoder.encode("args[username]", "utf-8")+"="+URLEncoder.encode(eduId, "utf-8")+"&format=JSON&method=Public.mobile"
						+"&ts="+timestamp;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			//对拼接后的字符串加密得到sign
			String sign=CommonUtil.md5(paramStrEncode);
			try {
				//拼接字符串加上剩余的参数encode后的拼接得到最终的参数字符串
				paramStrEncode += "&"+URLEncoder.encode("method", "utf-8")+"="+URLEncoder.encode("Public.mobile", "utf-8")+
						"&"+URLEncoder.encode("format", "utf-8")+"="+URLEncoder.encode("JSON", "utf-8")+
						"&"+URLEncoder.encode("ts", "utf-8")+"="+URLEncoder.encode(timestamp, "utf-8")+
						"&"+URLEncoder.encode("sign", "utf-8")+"="+URLEncoder.encode(sign, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String result = sendPost(url, paramStrEncode);//发送post
			JSONObject resultJobj = JSONObject.fromObject(result);//将结果转为json {"status":1,"data":{"10234357":"15901123273"}}
			JSONObject dataJobj = JSONObject.fromObject(resultJobj.getString("data"));//内部data转为json
			
			return dataJobj.size()>0&&dataJobj.containsKey(eduId)?dataJobj.getString(eduId):null;//根据eduId获取手机号
		}
		
	}
	
	public static Map<String,Object> getStuInfo(String eduId){	    	 
//		 eduId="test02";
		 String paramStrEncode = "";
		 String url = "";
		//通过eduId验证用户是否存在
		 String token = compute(eduId);
		 //验证邮箱是否被绑定
//		 url="http://211.153.82.39/usercenter/rest/user/getUserInfo";
		 //测试用http://192.168.136.176:8181/
//		 String caip = courseRepository.querySysParam("caIp").toString();
		 url="http://211.153.82.39/usercenter/rest/user/getUserInfo";
		 paramStrEncode="eduId="+eduId+"&token="+token;
		 String result = UserUtil.sendPost(url, paramStrEncode);//发送post
		 JSONObject resultJobj = JSONObject.fromObject(result);//将结果转为json {status:'0',"message":"未绑定",}{status:'1',"message":"邮箱已与其他用户绑定",}
		 String status = resultJobj.getString("status");
		 if(!status.equals("0")){
			 return null;
		 }else{
			 String userInfo = resultJobj.getString("userInfo");
			 JSONObject dataJobj = null;
			 if(userInfo != null ){
				 dataJobj = JSONObject.fromObject(userInfo);
			 }
//			 JSONObject dataJobj = JSONObject.fromObject(resultJobj.getString("userInfo"));//内部data转为json
			 Map<String,Object> stuInfo = dataJobj;
			 return stuInfo;
		 }		 
	 }
	
	public static String compute(String input) {
	    MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] resbyte = md.digest(input.getBytes());
			StringBuffer hexValue = new StringBuffer();
		    for (int i = 0; i < resbyte.length; i++) {
		        int val = ((int) resbyte [i]) & 0xff;
		            if (val < 16)
		                hexValue.append("0");
		        hexValue.append(Integer.toHexString(val));
		     }
		    return hexValue.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	    
	    
	}
	
	public static void main(String[] args) {
	}
	
	public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }    
	/**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param isproxy
     *               是否使用代理模式
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost1(String url, String param) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) realUrl.openConnection();
            // 打开和URL之间的连接

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");    // POST方法


            // 设置通用的请求属性

            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            conn.connect();

            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数
            out.write(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
        
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
}
