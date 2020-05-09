package com.ichzh.physicalFitness.run;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ichzh.physicalFitness.conf.SelfConfig;
import com.ichzh.physicalFitness.domain.AccessToken;
import com.ichzh.physicalFitness.domain.Bills;
import com.ichzh.physicalFitness.domain.WeChatToken;
import com.ichzh.physicalFitness.model.GoodsOrder;
import com.ichzh.physicalFitness.service.OrderService;
import com.ichzh.physicalFitness.service.ScheduleReminderService;
import com.ichzh.physicalFitness.service.SendTemplateMessageService;
import com.ichzh.physicalFitness.service.UserService;
import com.ichzh.physicalFitness.service.WeChatService;

import lombok.extern.slf4j.Slf4j;

/**
 * 定时任务
 */
@Component("timeTask")
@Slf4j
public class TimeTask implements CommandLineRunner {

    // 定时任务线程池(只有一条线程)
    public static final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

//    // 小程序全局唯一后台接口调用凭据对象
//    public static final AccessToken accessToken  = new AccessToken();
//
//    // 公众号 AccessToken
//    public static final AccessToken accessTokenTencent = new AccessToken();

    @Resource(name = "weChatService")
    private WeChatService weChatService;

    @Resource(name = "orderService")
    private OrderService orderService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "selfConfig")
    private SelfConfig selfConfig;
    
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    
    @Autowired
    private ScheduleReminderService scheduleReminderService;
    @Autowired
    private SendTemplateMessageService sendTemplateMessageService;
    
    /**
     * 初始化运行功能
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        // 初始化微信相关配置
        weChatService.initWeChatConfig();
        // 初始化获取 accessToken_小程序
        weChatService.getAccessToken(selfConfig.getAppid(), selfConfig.getSecret());
        //公众号的accessToken
        weChatService.getAccessToken(selfConfig.getPublicAppId(), selfConfig.getPublicAppSecret());
    }

    /**
     * 每天定时对账
     */
    @Scheduled(cron = "${timer.cron}")
    public void checkOrder() throws ParseException {
    	if (selfConfig.getIfStartScheduledTask() == null || selfConfig.getIfStartScheduledTask().equals("0")) {
    		// 不执行定时任务
    		return;
    	}
        log.info("现在是 " + DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "，开始对账");
        Date today = new Date();
        // 取昨天的日期
        String yesterday = DateFormatUtils.format(DateUtils.addDays(today, -1), "yyyyMMdd");
        String data = weChatService.getBills(yesterday, "ALL");
        if (StringUtils.isEmpty(data) || data.contains("<xml>")) {
            log.info("现在是 " + DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "，对账出错，无法完成");
            weChatService.saveStatementInfo(new Bills(), data, null);
            return;
        }
        // 账单信息是文本表格，解析为对象
        Bills bills = weChatService.formatBills(data, "ALL");
        // 昨天的开始时间；
        Date begin = DateUtils.parseDate(yesterday, "yyyyMMdd");
        // 今天的开始时间
        Date end = DateUtils.parseDate(DateFormatUtils.format(today, "yyyy-MM-dd"), "yyyy-MM-dd");
        // 为了防止本地的时间和微信服务的时间误差，需要起始时间和结束时间分别延申15分钟
        begin = DateUtils.addMinutes(begin, -15);
        end = DateUtils.addMinutes(end, 15);
        // 对账
        List<GoodsOrder> goodsOrders = orderService.statementAccount(bills, begin, end);
        log.info("现在是 " + DateFormatUtils.format(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "，对账成功");
        // 将对账的数据信息写入到文本中，并存入指定位置
        String path = weChatService.saveStatementInfo(bills, data, goodsOrders);
        log.info("对账明细地址：" + path);
    }

    /**
     * 间隔指定时间，定时刷新qpi调用凭证
     */
    @Scheduled(cron = "${timer.cron1}")
    public void fetchAccessToken() {
    	if (selfConfig.getIfStartScheduledTask() == null || selfConfig.getIfStartScheduledTask().equals("0")) {
    		// 不执行定时任务
    		return;
    	}
        weChatService.getAccessToken(selfConfig.getAppid(), selfConfig.getSecret());
        weChatService.getAccessToken(selfConfig.getPublicAppId(), selfConfig.getPublicAppSecret());
    }

    /**
     * 异步定时刷新调用凭证
     * @param delay
     * @param timeUnit
     */
    public void fetchAccessToken(long delay, TimeUnit timeUnit) {
    	
        Runnable runnable = () -> {
            weChatService.getAccessToken(selfConfig.getAppid(), selfConfig.getSecret());
            weChatService.getAccessToken(selfConfig.getPublicAppId(), selfConfig.getPublicAppSecret());
        };
        scheduledExecutorService.schedule(runnable, delay, timeUnit);
    }

    /**
     * 订单生成后，延迟进行查询订单对账
     *
     * @param goodsOrder 订单
     */
    public void queryAndCheckOrder(GoodsOrder goodsOrder, long delay) {
        // 使用异步线程执行定期任务
        Runnable runnable = () -> orderService.queryAndCheckOrder(goodsOrder);
        // 定时对账
        scheduledExecutorService.schedule(runnable, delay, TimeUnit.MINUTES);
    }

    /**
     * 关闭订单
     * @param goodsOrder
     */
    public void closeOrder(GoodsOrder goodsOrder, long delay) {
        // 使用异步线程执行关闭订单
        Runnable runnable = () -> orderService.closeWxOrder(goodsOrder);
        // 开启线程
        scheduledExecutorService.schedule(runnable, delay, TimeUnit.MINUTES);
    }

    /**
     * 刷新登录信息
     * @param request
     */
    public void fetchLoginInfo(HttpServletRequest request) {
        Runnable runnable = () -> userService.queryNewestMember(request);
        scheduledExecutorService.execute(runnable);
    }

    /**
     * 异步保存统一账户信息
     *
     * @param weChatToken
     */
    public void saveUnifiedAccount(WeChatToken weChatToken) {
        Runnable runnable = () -> userService.saveUnifiedAccount(weChatToken);
        scheduledExecutorService.execute(runnable);
    }

    /**
     * 异步保存统一账户信息
     *
     * @param openId
     */
    public void saveUnifiedAccount(String openId) {
        Runnable runnable = () -> userService.saveUnifiedAccount(openId);
        scheduledExecutorService.execute(runnable);
    }
    /**
     * 获取小程序的acccessToken
     * @return
     */
    public  AccessToken getAccessToken() {
    	return (AccessToken)redisTemplate.opsForValue().get("accessToken");
    }
    /**
     * 获取公众号的accessToken
     * @return
     */
    public AccessToken getAccessTokenTencent() {
    	return (AccessToken)redisTemplate.opsForValue().get("accessTokenTencent");
    }
    
    @Scheduled(cron = "${timer.cron2}")
    public void scheduleReminderPush() {
    	if (selfConfig.getIfStartScheduledTask() == null || selfConfig.getIfStartScheduledTask().equals("0")) {
    		// 不执行定时任务
    		return;
    	}
    	log.info("===============执行推送开始==================");
    	scheduleReminderService.scheduleReminderPush();
    	log.info("===============执行推送结束==================");
    }
    
    
    /**
	 * 关注公众号之后，向公众号中推送一条消息.
	 * @param gzhOpenId  公众号的openId
	 */
	public void asynPushGzhMsgAfterFollow(String gzhOpenId) {
		Runnable runnable = () -> scheduleReminderService.pushGzhMsgAfterFollow(gzhOpenId);
        scheduledExecutorService.execute(runnable);
	}
	
	public void asynPushBeansChange(Integer memberBeans, String openId, Integer getBeansNum) {
		Runnable runnable = () ->  sendTemplateMessageService.pushBeansChange(memberBeans, openId, getBeansNum);
		scheduledExecutorService.execute(runnable);
	}
}
