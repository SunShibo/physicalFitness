package com.ichzh.physicalFitness.service.impl;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ichzh.physicalFitness.conf.SelfConfig;
import com.ichzh.physicalFitness.run.TimeTask;
import com.ichzh.physicalFitness.service.MsgTypeService;
import com.ichzh.physicalFitness.util.HttpUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("getBeans")
public class GetBeansMsgServiceImpl implements MsgTypeService {

	@Resource(name = "selfConfig")
    private SelfConfig selfConfig;
    @Resource(name = "timeTask")
    private TimeTask timeTask;
	
	/**
	 * 获得豆或消费豆之后向用户推送小程序消息
	 * params  key1: openId 向该会员发送消息
	 *         key2: beansNum 获得或消费豆的数量
	 */
	public void sendMessage(Map<String, String> params, HttpServletRequest request) throws URISyntaxException {
		
		//需发消息的会员
		String openId = params.get("openId");
		//获得或消费豆的数量
		String beansNum = params.get("beansNum");
		
		if (StringUtils.isEmpty(openId) || StringUtils.isEmpty(beansNum)) {
			return;
		}
		
		
	}

	@Override
	public String sendServiceMessage(Map<String, String> params, HttpServletRequest request) throws URISyntaxException {
		
		String url = selfConfig.getCustomerServiceMessageSendUrl() + "?access_token=" + timeTask.getAccessToken().getAccess_token();
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", timeTask.getAccessToken().getAccess_token());
        map.put("touser", params.get("openId"));    
        
        //小程序模板消息对象
        Map<String, Object> weappTemplateMsg = new HashMap<String, Object>();
        //小程序模板ID
        weappTemplateMsg.put("template_id", "");
        //小程序页面路径
        weappTemplateMsg.put("page", "pages/index/index");
        //小程序模板消息formid
        weappTemplateMsg.put("form_id", "");
        
        //小程序消息内容
        Map<String, Object> weappMsgContent = new HashMap<String, Object>();
        weappTemplateMsg.put("data", weappMsgContent);
        
        weappMsgContent.put("keyword1", new HashMap<String, String>().put("value", ""));
        weappMsgContent.put("keyword2", new HashMap<String, String>().put("value", ""));
        weappMsgContent.put("keyword3", new HashMap<String, String>().put("value", ""));
        weappMsgContent.put("keyword4", new HashMap<String, String>().put("value", ""));
        //小程序消息内容中加粗显示的关键字
        weappTemplateMsg.put("emphasis_keyword", "keyword1.DATA");
        
        //设置小程序模板消息对象
        map.put("weapp_template_msg", weappTemplateMsg);
        
        //公众号模板消息对象
        Map<String, Object> mpTemplateMsg = new HashMap<String, Object>();
        //公众号appid，要求与小程序有绑定且同主体
        mpTemplateMsg.put("appid", "");
        //公众号模板id
        mpTemplateMsg.put("template_id", "");
        //公众号模板消息所要跳转的url
        mpTemplateMsg.put("url", "");
        //公众号模板消息所要跳转的小程序，小程序的必须与公众号具有绑定关系
        Map<String, String> miniprogram = new HashMap<String, String>();
        mpTemplateMsg.put("miniprogram", miniprogram);
        //小程序APPID
        miniprogram.put("appid", selfConfig.getAppid());
        miniprogram.put("pagepath", "pages/index/index");
        
        //公众号模板消息的数据
        Map<String, Object> mpTemplateMsgContent = new HashMap<String, Object>();
        
        Map<String, String> firstMap = new HashMap<String, String>();
        firstMap.put("value", "");
        firstMap.put("color", "#173177");
        mpTemplateMsgContent.put("first", firstMap);
        
        Map<String, String> keyword1Map = new HashMap<String, String>();
        keyword1Map.put("value", "");
        keyword1Map.put("color", "#173177");
        mpTemplateMsgContent.put("first", keyword1Map);
        
//        map.put("msgtype", "text");
//        Map<String, Object> contentData = new HashMap<>();
//        map.put("text", contentData);
//        // 响应文本内容
//        String content = params.get("messageType");
//        content = String.format(content, request.getServerName(), request.getContextPath());
//        contentData.put("content", content);
        String s = HttpUtil.sendBodyJson(url, map, String.class);
        log.info("客服调用api向客户发送消息学豆变更消息：" + s);
        return s;
	}

}
