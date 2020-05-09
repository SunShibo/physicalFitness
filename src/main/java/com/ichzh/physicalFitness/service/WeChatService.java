package com.ichzh.physicalFitness.service;

import com.ichzh.physicalFitness.domain.*;
import com.ichzh.physicalFitness.model.GoodsOrder;
import com.ichzh.physicalFitness.service.impl.WeChatAccount;

import org.dom4j.Element;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

public interface WeChatService {

    /**
     * 按照规则生成签名
     * @param params 调用微信api的参数，不包括sign
     * @return
     */
    String getSign(Map<String, String> params);

    /**
     * map转xml
     * @param params
     * @return
     */
    String map2Xml(Map<String, String> params);

    /**
     * xml转map
     * @param xml
     * @return
     */
    Map<String,String> xml2map(String xml);

    /**
     * 从指定节点开始,递归遍历所有子节点
     * 数据存入map
     * @param node
     * @param map
     */
    void getNodes(Element node, Map<String, String> map);

    /**
     * 查询交易账单所需要的参数
     * @param yesterday
     * @param billType
     * @return
     */
    String getBills(String yesterday, String billType);

    /**
     * 下载账单
     * @param yesterday
     * @param billType
     * @return
     */
    String downloadbill(String yesterday, String billType);

    /**
     * 账单信息文本，解析为对象
     * @param data
     * @return
     */
    Bills formatBills(String data, String billType);

    /**
     * 根据账单类型和账单信息生成账单对象
     * @param billInfo
     * @param billType
     * @return
     */
    Order getOrderByBillType(String[] billInfo, String billType);

    /**
     * 将对账信息保存为本文类型到硬盘
     * @param data
     * @param goodsOrders
     */
    String saveStatementInfo(Bills bills, String data, List<GoodsOrder> goodsOrders);

    /**
     * 获取微信客户端发起微信支付所需的参数
     * @param prepayId
     * @return
     */
    Map<String, String> getWxPaymentParam(String prepayId);

    /**
     * 同一天的微信支付通知，写入到同一个文件
     * @param xml
     * @param goodsOrder
     * @return
     */
    String saveWxPayNotify(String xml, GoodsOrder goodsOrder);

    /**
     * 初始化微信参数配置
     */
    void initWeChatConfig();

    /**
     * 获取沙箱密钥
     * @return
     */
    Map<String, String> getSignKey();

    /**
     * 查询微信服务订单
     * @param orderId
     * @return
     */
    Map<String, String> queryWxOrder(String orderId);

    /**
     * 统一下单
     * @param payOrderParam
     * @return
     */
    Map<String, String> unifiedOrder(PayOrderParam payOrderParam);

    /**
     * 关闭订单
     * @param orderId
     * @return
     */
    Map<String, String> closeOrder(String orderId);

    /**
     * 获取小程序全局唯一后台接口调用凭据（access_token）
     * 获取公众号全局唯一后台接口调用凭据（access_token）
     * @param accessToken
     */
    void getAccessToken(String appid, String secret);

    /**
     * 根据申请客服配置的数据类型解析数据
     * @return
     */
    Map<String, String> getContactParam(HttpServletRequest request, HttpServletResponse response) throws IOException;

    /**
     * 响应微信客服推送
     * @param response
     */
    void responseData(String data, String contentType, HttpServletResponse response) throws IOException;

    /**
     * 申请开启微信的消息推送服务，需要验证是不是微信服务发起的get请求
     * @param weChatAuthentication
     * @param response
     */
    void checkWeChatAuthentication(WeChatAuthentication weChatAuthentication, HttpServletResponse response) throws IOException;

    /**
     * 发送订阅消息
     * 接口限制
     * 次数限制：开通支付能力的是3kw/日，没开通的是1kw/日。
     * 40003	touser字段openid为空或者不正确
     * 40037	订阅模板id为空不正确
     * 43101	用户拒绝接受消息，如果用户之前曾经订阅过，则表示用户取消了订阅关系
     * 47003	模板参数不准确，可能为空或者不满足规则，errmsg会提示具体是哪个字段出错
     * 41030	page路径不正确，需要保证在现网版本小程序中存在，与app.json保持一致
     * 详情：https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/subscribe-message/subscribeMessage.send.html
     *
     * @param subscribeMessage
     * @return
     */
    Map<String, String> sendSubscribeMessage(SubscribeMessage subscribeMessage) throws URISyntaxException;

    /**
     * 发送公众号模板消息
     *
     * @param templateMessage
     * @return
     */
    Map<String, String> sendTemplateMessage(TemplateMessage templateMessage) throws URISyntaxException;

    /**
     * 发送客服消息
     *
     * @param param
     * @return
     */
    Map<String, String> sendCustomMessage(Map<String, Object> param, AccessToken accessToken) throws URISyntaxException;

    /**
     * 获取微信公众号用户信息
     *
     * @param openId
     * @param lang
     * @return
     */
    WeChatAccount getWeChatAccount(String openId, String lang);
}
