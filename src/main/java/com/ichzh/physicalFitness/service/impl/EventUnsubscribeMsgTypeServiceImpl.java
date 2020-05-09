package com.ichzh.physicalFitness.service.impl;

import com.ichzh.physicalFitness.conf.SelfConfig;
import com.ichzh.physicalFitness.model.UnifiedAccount;
import com.ichzh.physicalFitness.repository.UnifiedAccountRepository;
import com.ichzh.physicalFitness.service.MsgTypeService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 公众号取消关注
 */
@Service("event-unsubscribe")
@Slf4j
public class EventUnsubscribeMsgTypeServiceImpl implements MsgTypeService {

    @Resource(name = "unifiedAccountRepository")
    private UnifiedAccountRepository unifiedAccountRepository;

    @Resource(name = "selfConfig")
    private SelfConfig selfConfig;

    @Override
    public void sendMessage(Map<String, String> params, HttpServletRequest request) {
        String openId = params.get("FromUserName");
        // 取消关注，需要删除对应的信息
        UnifiedAccount unifiedAccount = unifiedAccountRepository.findByAppIdAndOpenId(selfConfig.getPublicAppId(), openId);
        if (unifiedAccount == null) {
            return;
        }
        unifiedAccountRepository.deleteByUnifiedAccountId(unifiedAccount.getUnifiedAccountId());
    }

    @Override
    public String sendServiceMessage(Map<String, String> params, HttpServletRequest request) {
        return null;
    }
}
