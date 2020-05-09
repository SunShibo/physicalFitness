package com.ichzh.physicalFitness.web.api;

import com.ichzh.physicalFitness.conf.SelfConfig;
import com.ichzh.physicalFitness.domain.PayNotify;
import com.ichzh.physicalFitness.domain.WeChatAuthentication;
import com.ichzh.physicalFitness.model.GoodsOrder;
import com.ichzh.physicalFitness.run.TimeTask;
import com.ichzh.physicalFitness.service.MsgTypeService;
import com.ichzh.physicalFitness.service.OrderService;
import com.ichzh.physicalFitness.service.WeChatService;
import com.ichzh.physicalFitness.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 回调接收控制器
 */
@RestController
@RequestMapping("/")
@Slf4j
public class CallBackApi {

    @Resource(name = "weChatService")
    private WeChatService weChatService;

    @Resource(name = "orderService")
    private OrderService orderService;

    @Resource(name = "selfConfig")
    private SelfConfig selfConfig;

    @Resource(name = "timeTask")
    private TimeTask timeTask;

    /**
     * 微信回调支付结果
     * , consumes = MediaType.APPLICATION_XML_VALUE
     * @return
     */
    @RequestMapping(value = "/callBack/wxPayNotify", produces = MediaType.APPLICATION_XML_VALUE)
    public PayNotify wxPayNotify(HttpServletRequest request) throws IOException {
        log.info("微信回调支付结果数据开始处理" );
        PayNotify res = new PayNotify();
        // 获取body参数
        ServletInputStream inputStream = request.getInputStream();
        String xml = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        CommonUtil.cloneInputStream(inputStream);
        if (StringUtils.isEmpty(xml)) {
            res.setReturnCode("FAIL");
            res.setReturnMsg("body 参数为空");
            return res;
        }
        Map<String, String> map = weChatService.xml2map(xml);
        // 签名校验
        String sign = weChatService.getSign(map);
        System.out.println(sign);
        if (!sign.equals(map.get("sign"))) {
            res.setReturnCode("FAIL");
            res.setReturnMsg("签名失败");
            log.info("微信回调支付结果数据处理失败，签名不正确");
            return res;
        }
        // 只要签名成功，就返回SUCCESS
        res.setReturnCode("SUCCESS");
        // return 为响应结果
        if (!"SUCCESS".equals(map.get("return_code"))) {
            res.setReturnMsg("签名通过，响应码为不成功");
            return res;
        }
        // 订单id
        String orderId = map.get("out_trade_no");
        // 订单取消状态为3
        int orderStatus = 3;
        // result_code 为业务处理结果
        if ("SUCCESS".equals(map.get("result_code"))) {
            // 如果支付成功，则修改订单状态为2
            orderStatus = 2;
        }
        GoodsOrder goodsOrder = orderService.modifyGoodsOrderByOrderIdAndOrderStatus(orderId, orderStatus);
        String path = weChatService.saveWxPayNotify(xml, goodsOrder);
        log.info("微信回调支付结果数据完成处理，处理结果：" + path);
        return res;
    }

    /**
     * 客服消息处理
     * @return
     */
    @RequestMapping("/wechatclientservice/messagepush")
    public void customerService(WeChatAuthentication weChatAuthentication, HttpServletRequest request, HttpServletResponse response) throws IOException, URISyntaxException {
        log.info("客服消息处理开始");
        String method = request.getMethod();
        // get 请求是开启微信消息通知处理
        if ("GET".equals(method)) {
            weChatService.checkWeChatAuthentication(weChatAuthentication, response);
            return;
        }
        // 解析用户发给客服的消息（文本消息、图片消息、小程序卡片消息、进入会话事件）
        Map<String, String> params =  weChatService.getContactParam(request, response);
        if (params == null) {
            return;
        }
        // 消息类型
        String msgType = params.get("MsgType");
        // 当前业务仅处理文本消息
        if (!"text".equals(msgType)) {
            return;
        }
        // 获取实现类
        MsgTypeService msgTypeService = MsgTypeService.getInstance(msgType);
        // 响应用户业务信息
        msgTypeService.sendMessage(params, request);
        // 响应微信服务
        weChatService.responseData("success", "text/plain", response);
        log.info("客服消息处理结束");
    }

    /**
     * 为了方便调试客服信息，增加此api
     * @param content
     * @param msgType  好入学   幼升小   小升初
     * @return
     */
    @RequestMapping("/wechatclientservice/setResCustomer")
    public String setResCustomer(String msgType, String content) {
    	Map<String, String> resCustomer = selfConfig.getResCustomer();
    	String oldContent =  resCustomer.get(msgType);
    	if (StringUtils.isNotEmpty(content) && StringUtils.isNotEmpty(oldContent)) {
    		resCustomer.put(msgType, content);
    		return "success";
    	}
        return "fail";
    }

    /**
     * 微信公众号-接收消息和实践，并回复用户
     *
     * @param request
     * @param response
     */
    @RequestMapping("/wechatclientservice/receiveReply")
    public void receiveReply(WeChatAuthentication weChatAuthentication,HttpServletRequest request, HttpServletResponse response) throws IOException, URISyntaxException {
        log.info("公众号事件消息处理开始");
        String method = request.getMethod();
        // get 请求是开启微信消息通知处理
        if ("GET".equals(method)) {
            weChatService.checkWeChatAuthentication(weChatAuthentication, response);
            return;
        }
        // 解析公众号接收普通消息/接收时间推送的参数（xml）
        ServletInputStream inputStream = request.getInputStream();
        String xml = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        log.info("公众号接收普通消息/接收时间推送的参数：\n"+xml);
        CommonUtil.cloneInputStream(inputStream);
        Map<String, String> param = weChatService.xml2map(xml);
        // 异步刷新用户信息
        String openId = param.get("FromUserName");
        String event = param.get("Event");
        String msgType = param.get("MsgType");
        if (StringUtils.isNotEmpty(openId) && (!"event".equals(msgType) || !"unsubscribe".equals(event))) {
            timeTask.saveUnifiedAccount(openId);
        }
        // 判断是否是关注/取消关注事件，当前业务仅处理关注/取消关注事件
        if (!"event".equals(msgType) || !Arrays.asList("subscribe", "unsubscribe").contains(event)) {
            weChatService.responseData("", "text/plain", response);
            return;
        }
        // 获取实现类
        MsgTypeService msgTypeService = MsgTypeService.getInstance(msgType + "-" + event);
        msgTypeService.sendMessage(param, request);
        
        // 响应微信服务
        weChatService.responseData("success", "text/plain", response);
        log.info("公众号事件消息处理结束");
    }
}
