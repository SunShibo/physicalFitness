package com.ichzh.physicalFitness.domain;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;

import com.ichzh.physicalFitness.model.Commodity;
import com.ichzh.physicalFitness.model.GoodsOrder;
import com.ichzh.physicalFitness.model.Member;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Data
public class PayOrderParam {

    // 商品信息
    private Commodity commodity;

    // 登录的用户信息
    private Member member;

    // 生成的订单对象
    private GoodsOrder goodsOrder;

    // 订单生成日期
    private Date date;

    // 使用学豆数量
    private int payBeansNum;

    // 优惠的金额
    private BigDecimal discountAmount;

    // 客户端请求体
    private HttpServletRequest request;

    // redis 锁
    private String lockKey;

    // 操作结果
    private OperaResult operaResult;

    // 统一下单响应参数
    private Map<String, String> map;

    // 支付时间长度（毫秒）
    private long payTime;
}
