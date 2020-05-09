package com.ichzh.physicalFitness.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.conf.SelfConfig;
import com.ichzh.physicalFitness.domain.TemplateMessage;
import com.ichzh.physicalFitness.model.TemplateMsgSendLog;
import com.ichzh.physicalFitness.model.UnifiedAccount;
import com.ichzh.physicalFitness.repository.TemplateMsgSendLogRepository;
import com.ichzh.physicalFitness.repository.UnifiedAccountRepository;
import com.ichzh.physicalFitness.run.TimeTask;
import com.ichzh.physicalFitness.service.ICacheApplicationService;
import com.ichzh.physicalFitness.service.SendTemplateMessageService;
import com.ichzh.physicalFitness.util.CommonUtil;
import com.ichzh.physicalFitness.util.HttpUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("sendTemplateMessageService")
public class SendTemplateMessageServiceImpl implements SendTemplateMessageService {

	@Resource
	SelfConfig selfConfig;
	
	@Resource(name = "timeTask")
    TimeTask timeTask;
	@Resource
	TemplateMsgSendLogRepository  templateMsgSendLogRepository;
	@Resource
	ICacheApplicationService cacheApplicationService;
	@Autowired
	UnifiedAccountRepository unifiedAccountRepository;
	
	/**
	 * 发送入学日程通知
	 * @param gzhOpenId 公众号openId
	 * @param first
	 * @param keyword1
	 * @param keyword2
	 * @param remark
	 * @param sendContent 用于记入发送模板消息日志的内容
	 */
	public void sendEnrollmentScheduleMsg(String gzhOpenId, String first, String keyword1, String keyword2, String sendContent) {
		
		if (StringUtils.isEmpty(gzhOpenId) || StringUtils.isEmpty(first) || StringUtils.isEmpty(keyword1) 
				|| StringUtils.isEmpty(keyword2)) {
			return;
		}
		//如果已经发送过相同的内容，则不发送
		if (cacheApplicationService.checkTemplateMsgIfSended(sendContent)) {
			return;
		}
		//公众号模板消息发送地址
		String url = selfConfig.getTemplateMessageSendUrl()+"?access_token="+timeTask.getAccessTokenTencent().getAccess_token();
		TemplateMessage templateMessage = selfConfig.getTemplateMessageMap().get("scheduleReminder");
        templateMessage.setTouser(gzhOpenId);
        Map<String, Map<String, String>> data = templateMessage.getData();
        
        Map<String, String> firstMap = data.get("first");
        firstMap.put("value", first);
        data.put("first", firstMap);
        
        Map<String, String> keyword1Map = data.get("keyword1");
        keyword1Map.put("value", keyword1);
        data.put("keyword1", keyword1Map);
        
        Map<String, String> keyword2Map = data.get("keyword2");
        keyword2Map.put("value", keyword2);
        data.put("keyword2", keyword2Map);
        
        templateMessage.setData(data);
        
        try
        {
        	String retValue = HttpUtil.sendBodyJson(url, templateMessage, String.class);
        	log.info(retValue);
        	//发送成功
        	if (retValue.contains("ok")) {
        		TemplateMsgSendLog templateMsgSendLog = new TemplateMsgSendLog();
        		templateMsgSendLog.setGzhOpenId(gzhOpenId);
        		templateMsgSendLog.setSendContent(sendContent);
        		templateMsgSendLog.setSendTime(new Date());
        		templateMsgSendLogRepository.saveAndFlush(templateMsgSendLog);
        		cacheApplicationService.writeTemplateMsgToCache(sendContent);
        	}
        	
        }catch(Exception ex) {
        	log.error("发送入学日程通知失败", ex);
        }
	}

	/**
	 * 发送获得学豆的通知
	 * @param memberId    会员ID
	 * @param openId      小程序openId
	 * @param getBeansNum 增加的学豆数.
	 */
	public void pushBeansChange(Integer memberBeans, String openId, Integer getBeansNum) {
		
		List<UnifiedAccount> unifiedAccounts = unifiedAccountRepository.queryUnifiedAccountBy(openId);
		if (unifiedAccounts != null && unifiedAccounts.size() > 0) {
			//公众号openId
			String gzhOpenId = unifiedAccounts.get(0).getOpenId();
			 //发送获得学豆记录
	        String first = "您的学豆本次增加了"+getBeansNum+"个";
	        String keyword1 = "学豆";
	        String keyword2 = "现在可使用学豆"+ memberBeans +"个";
	        
	        String sendContent = CommonUtil.getUUID()+"_"+openId;
	        sendEnrollmentScheduleMsg(gzhOpenId, first, keyword1, keyword2, sendContent);
		}
		
	}

	
	
}
