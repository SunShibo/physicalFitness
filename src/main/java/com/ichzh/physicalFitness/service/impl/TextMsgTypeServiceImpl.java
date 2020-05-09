package com.ichzh.physicalFitness.service.impl;

import com.ichzh.physicalFitness.conf.SelfConfig;
import com.ichzh.physicalFitness.run.TimeTask;
import com.ichzh.physicalFitness.service.MsgTypeService;
import com.ichzh.physicalFitness.util.HttpUtil;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Service("text")
@Slf4j
public class TextMsgTypeServiceImpl implements MsgTypeService {

    @Resource(name = "selfConfig")
    private SelfConfig selfConfig;
    @Resource(name = "timeTask")
    private TimeTask timeTask;

    @Override
    public void sendMessage(Map<String, String> params, HttpServletRequest request) throws URISyntaxException {
    	
    	Map<String, String> resCustomer = selfConfig.getResCustomer();
    	String content = resCustomer.get(params.get("Content"));
        // 用户在客服中输入好入学才受理
        if (StringUtils.isEmpty(content)) {
            return;
        }
        //key 供方法sendServiceMessage 取值用，无特别意义。
        params.put("messageType", content);
        // 当微信服务系统响应错误码为-1时，则需要重复调用发送客服信息，这里最多发送50次
        for (int i = 0; i < 50; i++) {
            String s = this.sendServiceMessage(params, request);
            JSONObject data = JSONObject.fromObject(s);
            if (!Integer.valueOf(-1).equals(data.get("errcode"))) {
                break;
            }
        }
    }

    @Override
    public String sendServiceMessage(Map<String, String> params, HttpServletRequest request) throws URISyntaxException {
        String url = selfConfig.getCustomerServiceMessageSendUrl() + "?access_token=" + timeTask.getAccessToken().getAccess_token();
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", timeTask.getAccessToken().getAccess_token());
        map.put("touser", params.get("FromUserName"));
        map.put("msgtype", "text");
        Map<String, Object> contentData = new HashMap<>();
        map.put("text", contentData);
        // 响应文本内容
        String content = params.get("messageType");
        content = String.format(content, request.getServerName(), request.getContextPath());
        contentData.put("content", content);
        String s = HttpUtil.sendBodyJson(url, map, String.class);
        log.info("客服调用api向客户发送消息响应数据：" + s);
        return s;
    }

}
