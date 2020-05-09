package com.ichzh.physicalFitness.service.impl;

import com.ichzh.physicalFitness.conf.SelfConfig;
import com.ichzh.physicalFitness.domain.AppletCard;
import com.ichzh.physicalFitness.run.TimeTask;
import com.ichzh.physicalFitness.service.MsgTypeService;
import com.ichzh.physicalFitness.service.WeChatService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * 公众号关注事件处理
 * 
 */
@Service("event-subscribe")
@Slf4j
public class EventSubscribeMsgTypeServiceImpl implements MsgTypeService {

    @Resource(name = "selfConfig")
    private SelfConfig selfConfig;

    @Resource(name = "weChatService")
    private WeChatService weChatService;
    
    @Resource(name = "timeTask")
    private TimeTask timeTask;

    @Override
    public void sendMessage(Map<String, String> params, HttpServletRequest request) throws URISyntaxException {
        // 参数转换
        Map<String, Object> map = new HashMap<>();
        // 发送文本信息的内容
        map.put("touser", params.get("FromUserName"));
        map.put("msgtype", "text");
        Map<String, Object> text = new HashMap<>();
        map.put("text", text);
        text.put("content", selfConfig.getTextMap().get("attentionContent"));
        weChatService.sendCustomMessage(map, timeTask.getAccessTokenTencent());
        // 然后发送小卡片（文本消息发送不成功，也发送小程序卡片消息）
        map.put("msgtype", "miniprogrampage");
        Map<String, Object> miniprogrampage = new HashMap<>();
        map.put("miniprogrampage", miniprogrampage);
        AppletCard appletCard = selfConfig.getAppletCardMap().get("attention");
        miniprogrampage.put("title", appletCard.getTitle());
        miniprogrampage.put("appid", appletCard.getAppid());
        miniprogrampage.put("pagepath", appletCard.getPagepath());
        miniprogrampage.put("thumb_media_id", appletCard.getThumbMediaId());
        weChatService.sendCustomMessage(map, timeTask.getAccessTokenTencent());
        //关注公众号后向公众号发送入学日程消息
        timeTask.asynPushGzhMsgAfterFollow(params.get("FromUserName"));
    }

    @Override
    public String sendServiceMessage(Map<String, String> params, HttpServletRequest request) {
        return null;
    }
}
