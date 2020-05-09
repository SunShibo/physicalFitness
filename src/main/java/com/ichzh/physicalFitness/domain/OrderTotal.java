package com.ichzh.physicalFitness.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 微信交易账单中的统计信息
 */
@Data
public class OrderTotal {

    // 总交易单数
    private int orderNum;

    // 总交易额
    private BigDecimal transactionAmount;

    // 总退款金额
    private BigDecimal refundAmount;

    // 总代金券或立减优惠退款金额
    private BigDecimal discountRefundAmount;

    // 手续费总金额
    private BigDecimal serviceCharge;
}
