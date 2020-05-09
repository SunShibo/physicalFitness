package com.ichzh.physicalFitness.service.impl;

import com.ichzh.physicalFitness.conf.AppInfo;
import com.ichzh.physicalFitness.conf.SelfConfig;
import com.ichzh.physicalFitness.domain.*;
import com.ichzh.physicalFitness.model.GoodsOrder;
import com.ichzh.physicalFitness.run.TimeTask;
import com.ichzh.physicalFitness.service.ResponseService;
import com.ichzh.physicalFitness.service.WeChatService;
import com.ichzh.physicalFitness.util.CommonUtil;
import com.ichzh.physicalFitness.util.HttpUtil;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.dom4j.*;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service("weChatService")
@Slf4j
public class WeChatServiceImpl implements WeChatService {

    @Resource(name = "selfConfig")
    private SelfConfig selfConfig;

    @Resource(name = "responseService")
    private ResponseService responseService;

    @Resource(name = "appInfo")
    private AppInfo appInfo;

    @Resource(name = "timeTask")
    private TimeTask timeTask;
    
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public String getSign(Map<String, String> params) {
        Set<String> set = params.keySet();
        List<String> list = new ArrayList<>(set);
        // 按照参数名ASCII码从小到大排序（字典序）
        Collections.sort(list);
        // 使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串
        StringBuilder sb = new StringBuilder();
        for (String key : list) {
            String value = params.get(key);
            // 空值不能参与签名
            if (StringUtils.isNotEmpty(value) && !"sign".equals(key)) {
                sb.append(key).append("=").append(value).append("&");
            }
        }
        String stringSignTemp = sb.toString() + "key=" + selfConfig.getKey();
        String sign = null;
        if ("MD5".equals(selfConfig.getSignType())) {
            sign = DigestUtils.md5Hex(stringSignTemp).toUpperCase();
        } else if ("HMAC-SHA256".equals(selfConfig.getSignType())) {
            sign = HmacUtils.hmacSha256Hex(selfConfig.getKey(), stringSignTemp).toUpperCase();
        }
        return sign;
    }

    @Override
    public String map2Xml(Map<String, String> params) {
        Set<String> set = params.keySet();
        // List<String> list = new ArrayList<>(set);
        // 按照参数名ASCII码从小到大排序（字典序）
        // Collections.sort(list);
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version='1.0' encoding='UTF-8' standalone='yes'?><xml>");
        for (String key : set) {
            Object value = params.get(key);
            if (value != null && !"".equals(value)) {
                sb.append("<").append(key).append(">").append(value).append("</").append(key).append(">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    @Override
    public Map<String, String> xml2map(String xml) {
        Map<String, String> map = new HashMap<>();
        try {
            Document document = DocumentHelper.parseText(xml);
            Element root = document.getRootElement();//获取根节点
            this.getNodes(root, map);//从根节点开始遍历所有节点
        } catch (DocumentException e) {
            log.error("xml转map出错", e);
        }
        return map;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void getNodes(Element node, Map<String, String> map) {
        //当前节点的名称、文本内容和属性
        map.put(node.getName(), node.getTextTrim());
        //当前节点的所有属性的list
        List<Attribute> listAttr = node.attributes();
        //遍历当前节点的所有属性
        for (Attribute attr : listAttr) {
            //属性名称
            String name = attr.getName();
            //属性的值
            String value = attr.getValue();
            map.put(name, value);
        }

        //递归遍历当前节点所有的子节点
        List<Element> listElement = node.elements();//所有一级子节点的list
        for (Element e : listElement) {//遍历所有一级子节点
            getNodes(e, map);//递归
        }
    }

    @Override
    public String getBills(String yesterday, String billType) {
        // 需要重新发送请求的错误码
        List<String> requeryCodes = Arrays.asList("100", "20003", "20100");
        String data = null;
        // 如果错误码是100，20003，20100时，需要重新发送请求，这里限定最多发送50次
        for (int i = 0; i < 50; i++) {
            // 发送http获取对账信息
            data = this.downloadbill(yesterday, billType);
            // 返回数据正常，则退出
            if (!data.contains("<xml>")) {
                return data;
            }
            // 如果包含xml节点，表示返回的是错误信息，解析成map
            Map<String, String> errorInfoMap = this.xml2map(data);
            // 如果错误码是100，20003，20100，则重新发送请求，否则退出循环
            if (!requeryCodes.contains(errorInfoMap.get("error_code"))) {
                return data;
            }
        }
        return data;
    }

    @Override
    public String downloadbill(String yesterday, String billType) {
        Map<String, String> params = new HashMap<>();
        params.put("appid", selfConfig.getAppid());
        params.put("mch_id", selfConfig.getMchId());
        params.put("nonce_str", CommonUtil.getUUID());
        params.put("bill_date", yesterday);
        params.put("bill_type", billType);
        String sign = this.getSign(params);
        params.put("sign", sign);
        // 转为 xml 字符串
        String xmlParams = this.map2Xml(params);
        return HttpUtil.sendBodyXml(selfConfig.getDownloadbillUrl(), xmlParams, String.class);
    }

    @Override
    public Bills formatBills(String data, String billType) {
        Bills bills = new Bills();
        List<Order> orderList = new ArrayList<>();
        bills.setOrderList(orderList);
        // 根据换行符解析
        String[] split = data.split("\n");
        // 解析出订单列表，遍历不包括表头和末尾两行
        for (int i = 1; i < split.length - 2; i++) {
            String billData = split[i];
            String[] billInfo = billData.split(",");
            Order order = this.getOrderByBillType(billInfo, billType);
            if (order != null) {
                // 符合条件的订单才受理
                orderList.add(order);
            } else {
                // 累计不处理的订单数量
                bills.setNotProcessedCont(bills.getNotProcessedCont() + 1);
            }
        }
        return bills;
    }

    @Override
    public Order getOrderByBillType(String[] billInfo, String billType) {
        if ("ALL".equals(billType)) {
            return this.getBillTypeAllOrder(billInfo);
        }
        return null;
    }

    /**
     * 当账单类型为ALL时生成订单对账对象
     * @return
     */
    private Order getBillTypeAllOrder(String[] billInfo) {
        // 微信退款单号，如果不为0表示该条数据为退款
        String weChatRefundId = billInfo[14].substring(1);
        // 交易状态；SUCCESS—支付成功，说明该行数据为一笔支付成功的订单 ；REFUND—转入退款，说明该行数据为一笔发起退款成功的退款单 ；REVOKED—已撤销，说明该行数据为一笔成功撤销的撤销单
        String tradingStatus = billInfo[9].substring(1);
        // 退款业务
        if (!"`0".equals(billInfo[14]) || "REFUND".equals(tradingStatus)) {
            // TODO 暂时不处理;
            return null;
        }
        Order order = new Order();
        order.setTradingStatus(tradingStatus);
        // 微信订单号
        order.setWeChatOrderId(billInfo[5].substring(1));
        // 商户订单号
        order.setOrderId(billInfo[6].substring(1));
        // 总金额
        order.setOrderPriceReality(new BigDecimal(billInfo[12].substring(1)));
        return order;
    }

    @Override
    public String saveStatementInfo(Bills bills, String data, List<GoodsOrder> goodsOrders) {
        String fileName = "小程序对账信息/" + "对账信息_" + DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss") + ".csv";
        if (data.contains("<xml>")) {
            return responseService.writeFile(data.getBytes(Charset.forName("GB2312")), fileName, false);
        }
        // 文本格式成好看的模样
        StringBuilder sb = new StringBuilder(data);
        sb.append("\n\n\n\n");
        sb.append("本次对账，不处理的订单数：").append(bills.getNotProcessedCont()).append(" 条\n");
        sb.append("本次对账，无误的订单数：").append(bills.getRightCount()).append(" 条\n");
        sb.append("本次对账，纠正的订单数：").append(bills.getUpdateCount()).append(" 条\n");
        sb.append("本次对账，本地没有的订单数：").append(bills.getNoneCount()).append(" 条\n");
        if (bills.getUpdateCount() > 0) {
            sb.append("\n\n\n\n纠正后的订单信息：\n");
            sb.append(JSONArray.fromObject(goodsOrders).toString());
        }
        // 新写入数据
        return responseService.writeFile(sb.toString().getBytes(Charset.forName("GB2312")), fileName, false);
    }

    @Override
    public Map<String, String> getWxPaymentParam(String prepayId) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", selfConfig.getAppid());
        params.put("timeStamp", String.valueOf((System.currentTimeMillis() / 1000)));
        params.put("nonceStr", CommonUtil.getUUID());
        params.put("package", "prepay_id=" + prepayId);
        params.put("signType", selfConfig.getSignType());
        // 签名
        String sign = this.getSign(params);
        params.put("paySign", sign);
        params.remove("appId");
        return params;
    }

    @Override
    public String saveWxPayNotify(String xml, GoodsOrder goodsOrder) {
        long millis = System.currentTimeMillis();
        String fileName = "小程序支付通知/" + "支付通知处理信息_" + DateFormatUtils.format(millis, "yyyy-MM-dd") + ".txt";
        // 文本格式成好看的模样
        StringBuilder sb = new StringBuilder();
        sb.append("============================== ");
        sb.append(DateFormatUtils.format(millis, "yyyy-MM-dd HH:mm:ss"));
        sb.append(" =============================\n\n");
        sb.append("小程序的支付回调通知信息：\n").append(xml).append("\n\n\n");
        if (goodsOrder == null) {
            sb.append("本地订单处理：有异常，本地找不到该订单");
        } else {
            sb.append("本地订单处理：\n").append(goodsOrder);
        }
        sb.append("\n\n\n");
        // 同文件追加数据
        return responseService.writeFile(sb.toString().getBytes(Charset.forName("GB2312")), fileName, true);
    }

    @Override
    public void initWeChatConfig() {
        // 使用沙箱仿真测试系统，则处理响应的url，并获取沙箱密钥
        if (!selfConfig.isSandboxnew()) {
            return;
        }
        String unifiedorderUrl = selfConfig.getUnifiedorderUrl().replace("https://api.mch.weixin.qq.com", "https://api.mch.weixin.qq.com/sandboxnew");
        selfConfig.setUnifiedorderUrl(unifiedorderUrl);
        String downloadbillUrl = selfConfig.getDownloadbillUrl().replace("https://api.mch.weixin.qq.com", "https://api.mch.weixin.qq.com/sandboxnew");
        selfConfig.setDownloadbillUrl(downloadbillUrl);
        String orderqueryUrl = selfConfig.getOrderqueryUrl().replace("https://api.mch.weixin.qq.com", "https://api.mch.weixin.qq.com/sandboxnew");
        selfConfig.setOrderqueryUrl(orderqueryUrl);
        String closeorderUrl = selfConfig.getCloseorderUrl().replace("https://api.mch.weixin.qq.com", "https://api.mch.weixin.qq.com/sandboxnew");
        selfConfig.setCloseorderUrl(closeorderUrl);
        // 获取仿真测试密钥
        Map<String, String> map = this.getSignKey();
        if (!"SUCCESS".equals(map.get("return_code"))) {
            return;
        }
        String mchId = map.get("mch_id");
        if (StringUtils.isNotEmpty(mchId)) {
            selfConfig.setMchId(mchId);
        }
        String sandboxSignkey = map.get("sandbox_signkey");
        if (StringUtils.isNotEmpty(sandboxSignkey)) {
            selfConfig.setKey(sandboxSignkey);
        }
    }

    @Override
    public Map<String, String> getSignKey() {
        // 获取沙箱仿真测试密钥和商户号
        Map<String, String> params = new HashMap<>();
        params.put("mch_id", selfConfig.getMchId());
        params.put("nonce_str", CommonUtil.getUUID());
        String sign = this.getSign(params);
        params.put("sign", sign);
        String xmlParams = this.map2Xml(params);
        String xmlData = HttpUtil.sendBodyXml(selfConfig.getSignkeyUrl(), xmlParams, String.class);
        log.info("\n获取沙箱密钥结果：\n" + xmlData);
        return this.xml2map(xmlData);
    }

    @Override
    public Map<String, String> queryWxOrder(String orderId) {
        // 查询微信服务的订单信息，当错误码为 SYSTEMERROR 时，表示微信服务系统异常，需再发起查询；这里最多连续查询50次
        for (int i = 0; i < 50; i++) {
            Map<String, String> wxOrderInfo = this.queryWxOrderInfo(orderId);
            // 错误码不是系统错误，且返回状态码是SUCCESS，则退出遍历查询
            if (!"SYSTEMERROR".equals(wxOrderInfo.get("err_code")) && "SUCCESS".equals(wxOrderInfo.get("return_code"))) {
                return wxOrderInfo;
            }
        }
        return null;
    }

    /**
     * 查询微信订单信息
     * @param orderId
     * @return
     */
    private Map<String, String> queryWxOrderInfo(String orderId) {
        Map<String, String> params = new HashMap<>();
        params.put("appid", selfConfig.getAppid());
        params.put("mch_id", selfConfig.getMchId());
        params.put("out_trade_no", orderId);
        params.put("nonce_str", CommonUtil.getUUID());
        params.put("sign_type", selfConfig.getSignType());
        // 签名
        String sign = this.getSign(params);
        params.put("sign", sign);
        String xmlParam = this.map2Xml(params);
        String xmlData = HttpUtil.sendBodyXml(selfConfig.getOrderqueryUrl(), xmlParam, String.class);
        log.info("\n微信订单信息：\n" + xmlData);
        return this.xml2map(xmlData);
    }

    /**
     * 详细参数说明 https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=9_1
     *
     * @param payOrderParam
     * @return
     */
    @Override
    public Map<String, String> unifiedOrder(PayOrderParam payOrderParam) {
        HttpServletRequest request = payOrderParam.getRequest();
        // prepay_id 过期时间点
        Date timeExpire = DateUtils.addMinutes(payOrderParam.getGoodsOrder().getOrderTime(), selfConfig.getPayTime());
        payOrderParam.getGoodsOrder().setTimeExpire(timeExpire);
        // 统一下单参数
        Map<String, String> params = new HashMap<>();
        params.put("appid", selfConfig.getAppid());
        params.put("mch_id", selfConfig.getMchId());
        params.put("device_info", selfConfig.getDeviceInfo());
        params.put("nonce_str", CommonUtil.getUUID());
        params.put("sign_type", selfConfig.getSignType());
        params.put("body", appInfo.getName() + "-" + payOrderParam.getCommodity().getCmmoName());
        JSONObject attach = new JSONObject();
        attach.put("commodityId", payOrderParam.getGoodsOrder().getCommodityId());
        params.put("attach", attach.toString());
        params.put("out_trade_no", payOrderParam.getGoodsOrder().getOrderId());
        params.put("fee_type", "CNY");
        params.put("total_fee", String.valueOf(payOrderParam.getGoodsOrder().getOrderPriceReality().multiply(new BigDecimal(100)).intValue()));
        params.put("spbill_create_ip", CommonUtil.getIp(request));
        params.put("time_start", DateFormatUtils.format(payOrderParam.getGoodsOrder().getOrderTime(), "yyyyMMddHHmmss"));
        params.put("time_expire", DateFormatUtils.format(timeExpire, "yyyyMMddHHmmss"));
        params.put("notify_url", "https://" + request.getServerName() + request.getContextPath() + selfConfig.getNotifyUrl());
        params.put("trade_type", selfConfig.getTradeType());
        params.put("product_id", payOrderParam.getGoodsOrder().getCommodityId());
        params.put("openid", payOrderParam.getMember().getWeChatToken().getOpenid());
        // 如果开启沙箱环境，则body不能是中文；金额必须是101分
        if (selfConfig.isSandboxnew()) {
            params.put("body", "iGrow:haoruxue");
            params.put("total_fee", "101");
        }
        // 签名
        String sign = this.getSign(params);
        params.put("sign", sign);
        // 生成xml字符串
        String xml = this.map2Xml(params);
        log.info("统一下单xml参数：\n" + xml);
        // 发送统一下单请求
        String xmlData = HttpUtil.sendBodyXml(selfConfig.getUnifiedorderUrl(), xml, String.class);
        log.info("统一下单结果：\n" + xmlData);
        return this.xml2map(xmlData);
    }

    @Override
    public Map<String, String> closeOrder(String orderId) {
        // 关单操作，如果返回码不成功，或者错误码为 SYSTEMERROR 时，需要重新发起请求，这里最多发送50次
        for (int i = 0; i < 50; i++) {
            Map<String, String> map = this.closeWxOrder(orderId);
            // 错误码不是系统错误，且返回状态码是SUCCESS，则退出遍历查询
            if (!"SYSTEMERROR".equals(map.get("err_code")) && "SUCCESS".equals(map.get("return_code"))) {
                return map;
            }
        }
        return null;
    }

    /**
     * 关单
     * @param orderId
     * @return
     */
    private Map<String, String> closeWxOrder(String orderId) {
        // 关单参数
        Map<String, String> params = new HashMap<>();
        params.put("appid", selfConfig.getAppid());
        params.put("mch_id", selfConfig.getMchId());
        params.put("out_trade_no", orderId);
        params.put("nonce_str", CommonUtil.getUUID());
        params.put("sign_type", selfConfig.getSignType());
        // 签名
        String sign = this.getSign(params);
        params.put("sign", sign);
        // 发送关单请求
        String xml = this.map2Xml(params);
        String xmlData = HttpUtil.sendBodyXml(selfConfig.getCloseorderUrl(), xml, String.class);
        log.info("\n关单响应信息：" + xmlData);
        return this.xml2map(xmlData);
    }

    @Override
    public void getAccessToken(String appid, String secret) {
        Map<String, Object> param = new HashMap<>();
        param.put("appid", appid);
        param.put("secret", secret);
        param.put("grant_type", "client_credential");
        log.info("获取 access_token 参数：" + JSONObject.fromObject(param));
        String s = HttpUtil.sendGet(selfConfig.getTokenUrl(), param, String.class);
        AccessToken token = (AccessToken) JSONObject.toBean(JSONObject.fromObject(s), AccessToken.class);
        // 如果错误码为 -1，则1分钟后重试
        if (Integer.valueOf(-1).equals(token.getErrcode())) {
            timeTask.fetchAccessToken(1, TimeUnit.MINUTES);
            return;
        }
        // 更新登录凭证
//        BeanUtils.copyProperties(token, accessToken);
        // 将accessToken写入redis
        // 小程序的token
        if (selfConfig.getAppid().equals(appid)) {
        	redisTemplate.opsForValue().set("accessToken", token);
        }else {
        	redisTemplate.opsForValue().set("accessTokenTencent", token);
        }
        
        log.info("刷新 access_token 成功：" + token);
    }

    @Override
    public Map<String, String> getContactParam(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String message = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        CommonUtil.cloneInputStream(inputStream);
        log.info("用户发送给客服的消息：" + message);
        if (StringUtils.isEmpty(message)) {
            this.responseData("", "text/plain;charset=utf-8", response);
            return null;
        }
        Map<String, String> map = new HashMap<>();
        String contentType = request.getContentType();
        map.put("Content-Type", contentType);
        // 使用json
        if (contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
            return this.parseJsonMessage(map, message);
        }
        return null;
    }

    /**
     * 解析json参数
     * @param message
     * @return
     */
    @SuppressWarnings("unchecked")
    private Map<String, String> parseJsonMessage(Map<String, String> map, String message) {
        JSONObject jsonObject = JSONObject.fromObject(message);
        Set<String> set = jsonObject.keySet();
        for (String key : set) {
            Object value = jsonObject.get(key);
            if (value != null) {
                map.put(key, String.valueOf(value));
            }
        }
        return map;
    }

    @Override
    public void responseData(String data, String contentType, HttpServletResponse response) throws IOException {
        response.setContentType(contentType);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(data.getBytes());
        outputStream.flush();
        outputStream.close();
    }

    @Override
    public void checkWeChatAuthentication(WeChatAuthentication weChatAuthentication, HttpServletResponse response) throws IOException {
        log.info("校验是否为微信认证");
        if (weChatAuthentication == null) {
            this.responseData("访问成功", "text/plain;charset=utf-8", response);
            return;
        }
        if (StringUtils.isEmpty(weChatAuthentication.getTimestamp()) || StringUtils.isEmpty(weChatAuthentication.getNonce())) {
            this.responseData("访问成功", "text/plain;charset=utf-8", response);
            return;
        }
        // 将token、timestamp、nonce三个参数进行字典序排序
        List<String> list = Arrays.asList(selfConfig.getToken(), weChatAuthentication.getTimestamp(), weChatAuthentication.getNonce());
        Collections.sort(list);
        // 将三个参数字符串拼接成一个字符串进行sha1加密
        String join = StringUtils.join(list, "");
        String hex = DigestUtils.sha1Hex(join);
        // 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
        if (hex.equals(weChatAuthentication.getSignature())) {
            // 原样返回 echostr 参数内容
            this.responseData(weChatAuthentication.getEchostr(), "text/plain;charset=utf-8", response);
            return;
        }
        this.responseData("", "text/plain;charset=utf-8", response);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, String> sendSubscribeMessage(SubscribeMessage subscribeMessage) throws URISyntaxException {
        // 设置access_token
        String accessToken = timeTask.getAccessToken().getAccess_token();
        String url = selfConfig.getSubscribeMessageSendUrl() + "?access_token=" + accessToken;
        subscribeMessage.setAccess_token(accessToken);
        String result = HttpUtil.sendBodyJson(url, subscribeMessage, String.class);
        return JSONObject.fromObject(result);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, String> sendTemplateMessage(TemplateMessage templateMessage) throws URISyntaxException {
        log.info("向微信公众号发送模板信息参数：" + templateMessage);
        for (int i = 0; i < 50; i++) {
            String url = selfConfig.getTemplateMessageSendUrl() + "?access_token=" + timeTask.getAccessTokenTencent().getAccess_token();
            String s = HttpUtil.sendBodyJson(url, templateMessage, String.class);
            JSONObject data = JSONObject.fromObject(s);
            log.info("向微信公众号发送模板信息结果：" + s);
            if (!Integer.valueOf(-1).equals(data.get("errcode"))) {
                return data;
            }
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, String> sendCustomMessage(Map<String, Object> param, AccessToken accessToken) throws URISyntaxException {
        log.info("发送客服信息参数：" + JSONObject.fromObject(param));
        // 当微信服务系统响应错误码为-1时，则需要重复调用发送客服信息，这里最多发送50次
        for (int i = 0; i < 50; i++) {
            String url = selfConfig.getCustomerServiceMessageSendUrl() + "?access_token=" + accessToken.getAccess_token();
            String s = HttpUtil.sendBodyJson(url, param, String.class);
            log.info("调用客服api向客户发送消息响应数据：" + s);
            JSONObject data = JSONObject.fromObject(s);
            if (!Integer.valueOf(-1).equals(data.get("errcode"))) {
               return data;
            }
        }
        return null;
    }

    @Override
    public WeChatAccount getWeChatAccount(String openId, String lang) {
        if (StringUtils.isEmpty(lang)) {
            lang = "zh_CN";
        }
        for (int i = 0; i < 50; i++) {
            String url = selfConfig.getUserInfoUrl() + "?access_token=" + timeTask.getAccessTokenTencent().getAccess_token() + "&openid=" + openId + "&lang=" + lang;
            WeChatAccount weChatAccount = HttpUtil.sendGet(url, WeChatAccount.class);
            if (!Integer.valueOf(-1).equals(weChatAccount.getErrcode())) {
                return weChatAccount;
            }
        }
        return null;
    }
}
