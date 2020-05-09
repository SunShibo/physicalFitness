package com.ichzh.physicalFitness.service.impl;

import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.conf.Constant;
import com.ichzh.physicalFitness.service.MessageByLocaleService;

@Service
public class MessageByLocaleServiceImpl implements MessageByLocaleService {
	
	private MessageSource messageSource;
	
	@Autowired
	public MessageByLocaleServiceImpl(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public String getMessage(String key, Object... args) {
		Locale locale = LocaleContextHolder.getLocale();
		return messageSource.getMessage(key, args, locale);
	}

	@Override
	public String getMessage(HttpSession session, String key, Object... args) {
		
		String currentLang = (String)session.getAttribute(Constant.KEY_CURRENT_LANG);
		Locale locale = new Locale(currentLang);
		return messageSource.getMessage(key, args, locale);
	}
	
	

}
