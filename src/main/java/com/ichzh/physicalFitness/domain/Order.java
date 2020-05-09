package com.ichzh.physicalFitness.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 微信交易账单中的当日所有订单信息
 */
@Data
public class Order {

    // 交易时间
    private Date ms;

    // 公众账号ID
    private String appid;

    // 商户号
    private String mchId;

    // 子商户号
    private String childMchId;

    // 设备号
    private String  deviceNumber;

    // 微信订单号
    private String weChatOrderId;

    // 商户订单号
    private String orderId;

    // 用户标识
    private String memberWeChat;

    // 交易类型
    private String tradeType;

    // 交易状态
    private String tradingStatus;

    // 付款银行
    private String payingBank;

    // 货币种类
    private String currency;

    // 总金额
    private BigDecimal orderPriceReality;

    // 代金券或立减优惠金额
    private BigDecimal discountAmount;

    // 微信退款单号
    private String weChatRefundId;

    // 商户退款单号
    private String businessRefundId;

    // 退款金额
    private BigDecimal refundAmount;

    // 代金券或立减优惠退款金额
    private BigDecimal discountRefundAmount;

    // 退款类型
    private String refundType;

    // 退款状态
    private String refundStatus;

    // 商品名称
    private String cmmoName;

    // 商户数据包
    private String orderDescribe;

    // 手续费
    private BigDecimal serviceCharge;

    // 费率
    private String rate;
}
