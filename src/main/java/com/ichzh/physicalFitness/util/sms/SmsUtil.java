package com.ichzh.physicalFitness.util.sms;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.ichzh.physicalFitness.conf.SelfConfig;

@Slf4j
public class SmsUtil {
	
	private static final String USERNAME="sxcz";
	private static final String PASSWORD="014524";
	private static final String CHARSET="utf-8";
	private static final String URL="http://www.youxinyun.com:3070/Platform_Http_Service/servlet/SendSms";

	/**
	 * 发送短信
	 * @param mobile 手机号码
	 * @param messageContent 内容
	 * @return
	 */
	public static String sendMessage(String mobile, String messageContent){
		log.info("发送的短信内容：" + messageContent);
		if (!SelfConfig.getIsSms()) {
			return null;
		}
		log.info("==========调用发送短信方法=======");
		String Timestemp = getTimestemp();
		String Key = getKey(USERNAME, PASSWORD, Timestemp);
		Map<String, String> params = new HashMap<String, String>();
		params.put("UserName", USERNAME);
		params.put("Content", messageContent);
		params.put("Mobiles", mobile);
		params.put("CharSet", CHARSET);
		params.put("Key", Key);
		params.put("Timestemp", Timestemp);
		return post(URL, params);
	}
	
	public static String sendErrorMsg(String mobile){
		String str="【开放科学】定时任务执行失败！";
		String Timestemp=getTimestemp();
		String Key=getKey(USERNAME, PASSWORD, Timestemp);
		Map<String, String> params = new HashMap<String, String>();
		params.put("UserName", USERNAME);
		params.put("Content", str);
		params.put("Mobiles", mobile);
		params.put("CharSet", CHARSET);
		params.put("Key", Key);
		params.put("Timestemp", Timestemp);
		return post(URL, params);
	}
	
	
	public static String post(String url, Map<String, String> paramsMap) {
	    CloseableHttpClient client = HttpClients.createDefault();
	    String responseText = "";
	    CloseableHttpResponse response = null;
	    try {
	        HttpPost method = new HttpPost(url);
	        if (paramsMap != null) {
	            List<NameValuePair> paramList = new ArrayList<NameValuePair>();
	            for (Map.Entry<String, String> param : paramsMap.entrySet()) {
	                NameValuePair pair = new BasicNameValuePair(param.getKey(), param.getValue());
	                paramList.add(pair);
	            }
	            method.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
	        }
	        response = client.execute(method);
	        HttpEntity entity = response.getEntity();
	        if (entity != null) {
	            responseText = EntityUtils.toString(entity);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            response.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return responseText;
	}
	public static void main(String[] args) {
		System.out.println(sendErrorMsg("18201309099"));
	}
	
	public static String getKey(String userName, String password, String timestemp) {
		String key = "";
		try {
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(userName.getBytes());
			mdTemp.update(password.getBytes());
			mdTemp.update(timestemp.getBytes());
			key = bytesToHexString(mdTemp.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return key;
	}
	public static String getTimestemp() {
		return (new SimpleDateFormat("MMddHHmmss")).format(new Date());
	}
	public static String bytesToHexString(byte[] src) {
		String resultString = "";
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		resultString = stringBuilder.toString();
		stringBuilder = null;
		return resultString;
	}
}
