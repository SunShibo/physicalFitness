package com.ichzh.easy4enrolwas.test;

import net.sf.json.JSONObject;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.ichzh.physicalFitness.util.CommonUtil;
import com.ichzh.physicalFitness.util.HttpUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CodeTest {

	@Test
	public void t1() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String encode = bCryptPasswordEncoder.encode("111111");
		System.out.println(encode);
		System.out.println(encode.length());
	}
	
	@Test
	public void t2() {
		StringBuffer stringBuffer = new StringBuffer();
		System.out.println(stringBuffer);
	}

	@Test
	public void t3() {
		JSONObject attach = new JSONObject();
		attach.put("memberId", CommonUtil.getUUID());
		attach.put("commodityId", CommonUtil.getUUID());
		System.out.println(attach.toString().length());
		System.out.println(attach.toString());
	}

	@Test
	public void t4() {
		String localIp = CommonUtil.getLocalIp();
		System.out.println(localIp);
	}

	@Test
	public void t5() {
		System.out.println(MediaType.APPLICATION_XML);
	}

	@Test
	public void t6() {
		StringBuilder content = new StringBuilder();
		content.append("<rich-text nodes=\"");
		content.append("<h3>好入学-附近家长群</h3>");
		content.append("<a href='https://").append("%s").append("%s").append("/wechatclientservice/qrcode.html").append("'>点击跳二维码页面").append("</a>");
		content.append("\" type=\"text\"></rich-text>");
		String s = content.toString();
		System.out.println(s);
		System.out.println(String.format(s, "chzh-hrxs.qa.ichzh.com", "/easy4enrolwas"));
	}

	@Test
	public void t7() {
		RestTemplate restTemplate = new RestTemplate();
		HttpUtil httpUtil = new HttpUtil(restTemplate);

		String url = "https://chzh-hrxs.qa.ichzh.com/easy4enrolwas/wechatclientservice/setResCustomer";
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		// map.put("content", Collections.singletonList("\"<rich-text nodes=\"<h3>好入学-附近家长群</h3><a href='https://%s%s/wechatclientservice/qrcode.html'>点击跳二维码页面</a>\" type=\"text\"></rich-text>\""));
		map.put("content", Collections.singletonList("好入学-附近家长群\n\n<a href='https://%s%s/wechatclientservice/qrcode.html'>点击跳二维码页面</a>"));
		// map.put("content", Collections.singletonList("<view style='font-weight:bold;'>好入学-附近家长群</view>\n\n<a href='https://%s%s/wechatclientservice/qrcode.html'>点击跳二维码页面</a>"));
		String s = HttpUtil.sendPostForm(url, map, String.class);
		System.out.println(s);
	}

	@Test
	public void t8() {
		Map<String, Object> map = new HashMap<>();
		map.put("aaa", "aaaaa");
		Map<String, Object> param = new HashMap<>();
		param.put("zzzzzzzz", "sssssssssssss");
		map.put("ss", param);
		System.out.println(JSONObject.fromObject(map));
		System.out.println(JSONObject.fromObject(null));
	}

}
