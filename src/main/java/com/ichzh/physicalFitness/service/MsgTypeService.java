package com.ichzh.physicalFitness.service;

import javax.servlet.http.HttpServletRequest;

import com.ichzh.physicalFitness.util.ApplicationContextProviderUtil;

import java.net.URISyntaxException;
import java.util.Map;

/**
 * 微信客服消息类型服务
 */
public interface MsgTypeService {

    /**
     * 根据消息类型获取具体的实现类对象
     * @param MsgType
     * @return
     */
    static MsgTypeService getInstance(String MsgType) {
        return ApplicationContextProviderUtil.getBean(MsgType, MsgTypeService.class);
    }

    /**
     * 客服发送信息给用户
     * @param params
     */
    void sendMessage(Map<String, String> params, HttpServletRequest request) throws URISyntaxException;

    /**
     * 调用客服api推送消息
     *
     * @param params
     * @param request
     * @return
     * @throws URISyntaxException
     */
    String sendServiceMessage(Map<String, String> params, HttpServletRequest request) throws URISyntaxException;
}
