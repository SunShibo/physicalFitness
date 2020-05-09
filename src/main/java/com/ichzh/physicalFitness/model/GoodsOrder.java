package com.ichzh.physicalFitness.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单
 */
@Entity(name = "goods_order")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoodsOrder implements Serializable {

    // 订单号
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "order_id")
    private String orderId;

    // 会员ID
    @Column(name = "member_id")
    private String memberId;

    // API商品信息标识号
    @Column(name = "commodity_id")
    private String commodityId;

    // 下单时间
    @Column(name = "order_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date orderTime;

    // 订单金额 （单位：人民币）
    @Column(name = "order_price")
    private BigDecimal orderPrice;

    // 订单实付金额  （单位：人民币）
    @Column(name = "order_price_reality")
    private BigDecimal orderPriceReality;

    // 使用学豆数量
    @Column(name = "pay_beans_num")
    private Integer payBeansNum;

    // 支付方式 1：微信支付
    @Column(name = "order_payment")
    private Integer orderPayment;

    // 订单状态 订单状态 1: 待缴费 2：已缴费 3：取消
    @Column(name = "order_status")
    private Integer orderStatus;

    // 有效截止时间
    @Column(name = "valid_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date validTime;

    // 订单状态名称
    @Transient
    private String orderStatusName;

    // 订单更新信息
    @Transient
    private String updateMessage;

    // prepay_id 过期时间点
    @Transient
    private Date timeExpire;

    // 订单下的商品
    @Transient
    private Commodity commodity;
}
