package com.ichzh.physicalFitness.domain;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * 微信服务响应错误信息对象
 */
@Data
@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class PayNotify implements Serializable {

    // 返回状态码
    @XmlElement(name = "return_code")
    private String returnCode;

    // 错误码描述
    @XmlElement(name = "return_msg")
    private String returnMsg;

    // 小程序ID
    private String appid;

    // 商户号
    private String mch_id;

    // 设备号
    private String device_info;

    // 随机字符串
    private String nonce_str;

    // 签名
    private String sign;

    // 签名类型
    private String sign_type;

    // 业务结果
    private String result_code;

    // 错误代码
    private String err_code;

    // 错误代码描述
    private String err_code_des;

    // 用户标识
    private String openid;

    // 是否关注公众账号
    private String is_subscribe;

    // 交易类型
    private String trade_type;

    // 付款银行
    private String bank_type;

    // 订单金额
    private Integer total_fee;

    // 应结订单金额
    private Integer settlement_total_fee;

    // 货币种类
    private String fee_type;

    // 现金支付金额
    private Integer cash_fee;

    // 现金支付货币类型
    private String cash_fee_type;

    // 总代金券金额
    private Integer coupon_fee;

    // 代金券使用数量
    private Integer coupon_count;

    // 代金券类型
    private String coupon_type_$n;

    // 代金券ID
    private String coupon_id_$n;

    // 单个代金券支付金额
    private Integer coupon_fee_$n;

    // 微信支付订单号
    private String transaction_id;

    // 商户订单号
    private String out_trade_no;

    // 商家数据包
    private String attach;

    // 支付完成时间
    private String time_end;
}
