package com.ichzh.physicalFitness.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Component
@SuppressWarnings({"unused", "static-access"})
public class HttpUtil {

	private static RestTemplate restTemplate;

	@Autowired
	public HttpUtil(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public static <T> T sendGet(String url, Class<T> clazz) {
		return restTemplate.getForObject(url, clazz);
	}

	public static <T> T sendGet(String url, Map<String, Object> map, Class<T> clazz) {
		url = spliceUrl(url, map);
		return sendGet(url , clazz);
	}

	// 发送文件
	public static <T> T sendPost(String url, MultiValueMap<String, Object> multiPartBody, Class<T> clazz) throws URISyntaxException {
		RequestEntity<MultiValueMap<String, Object>> requestEntity = RequestEntity
				.post(new URI(url))
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.body(multiPartBody);
		ResponseEntity<T> entity = restTemplate.exchange(requestEntity, clazz);
		return entity.getBody();
	}

	// 发送 json
	public static <T> T sendBodyJson(String url, Object object, Class<T> clazz) throws URISyntaxException {
		RequestEntity<Object> requestEntity = RequestEntity.post(new URI(url)).contentType(MediaType.APPLICATION_JSON_UTF8).body(object);
		ResponseEntity<T> entity = restTemplate.exchange(requestEntity, clazz);
		return entity.getBody();
	}

	// 发送post
	public static <T> T sendPostForm(String url, MultiValueMap<String, String> map, Class<T> clazz) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
		HttpEntity<MultiValueMap<String, String>> r = new HttpEntity<>(map, headers);
		return restTemplate.postForObject(url, r, clazz);
	}

	// 发送 xml
	public static <T> T sendBodyXml(String url, String xmlData, Class<T> clazz) {
		HttpHeaders httpHeaders = new HttpHeaders();
		//httpHeaders.setContentType(MediaType.APPLICATION_XML);
		httpHeaders.set("Content-Type", "application/xml;charset=UTF-8");
		HttpEntity<String> httpEntity = new HttpEntity<>(xmlData, httpHeaders);
		ResponseEntity<T> responseEntity = restTemplate.postForEntity(url, httpEntity, clazz);
		return responseEntity.getBody();
	}

	// 拼接url和请求参数
	public static String spliceUrl(String url, Map<String, Object> map) {
		if (map == null) {
			return url;
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(url);
		stringBuilder.append("?");
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			stringBuilder.append(entry.getKey());
			stringBuilder.append("=");
			stringBuilder.append( entry.getValue());
			stringBuilder.append("&");
		}
		String s = stringBuilder.toString();
		if (s.endsWith("&")) {
			s = StringUtils.substringBeforeLast(s, "&");
		}
		return s;
	}
}
