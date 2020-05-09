package com.ichzh.physicalFitness.service;

import javax.servlet.http.HttpSession;

public interface MessageByLocaleService {
	/**
	 * 获取当前语言的消息。
	 * @param key 消息键名。
	 * @return 消息。
	 */
	public String getMessage(String key, Object... args);
	
	public String getMessage(HttpSession session, String key, Object... args);
}
