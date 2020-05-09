package com.ichzh.physicalFitness.web.api;

import com.ichzh.physicalFitness.conf.SelfConfig;
import com.ichzh.physicalFitness.domain.AppletCard;
import com.ichzh.physicalFitness.domain.SubscribeMessage;
import com.ichzh.physicalFitness.domain.TemplateMessage;
import com.ichzh.physicalFitness.domain.WeChatToken;
import com.ichzh.physicalFitness.model.GoodsOrder;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.repository.GoodsOrderRepository;
import com.ichzh.physicalFitness.run.TimeTask;
import com.ichzh.physicalFitness.service.MemberShareService;
import com.ichzh.physicalFitness.service.OrderService;
import com.ichzh.physicalFitness.service.ScheduleReminderService;
import com.ichzh.physicalFitness.service.UserService;
import com.ichzh.physicalFitness.service.WeChatService;
import com.ichzh.physicalFitness.util.HttpUtil;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/demo")
@Slf4j
public class LeiyunranApi {

    @Resource(name = "goodsOrderRepository")
    private GoodsOrderRepository goodsOrderRepository;

    @Resource(name = "weChatService")
    private WeChatService weChatService;

    @Resource(name = "timeTask")
    private TimeTask timeTask;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "orderService")
    private OrderService orderService;

    @Resource(name = "selfConfig")
    private SelfConfig selfConfig;

    @Resource(name = "memberShareService")
    private MemberShareService memberShareService;
    
    @Resource(name = "scheduleReminderService")
    private ScheduleReminderService scheduleReminderService;


    /**
     * 测试推送消息服务
     *
     * @return
     */
    @RequestMapping("/t12")
    public Object t12(HttpServletRequest request) throws URISyntaxException {
        SubscribeMessage subscribeMessage = memberShareService.getSubscribeMessage(request, "test");
        Map<String, String> stringStringMap = weChatService.sendSubscribeMessage(subscribeMessage);
        System.out.println(stringStringMap);
        return stringStringMap;
    }

   
    @RequestMapping("/t1")
    public void t1(HttpServletRequest request) {
    	scheduleReminderService.scheduleReminderPush();
    }
}
