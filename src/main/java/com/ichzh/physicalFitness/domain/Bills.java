package com.ichzh.physicalFitness.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Bills implements Serializable {

    // 订单列表
    private List<Order> orderList;

    // 订单统计
    private OrderTotal orderTotal;

    // 不处理的订单数（退款订单数）
    private int notProcessedCont;

    // 对账无误的订单数
    private int rightCount;

    // 对账有误需要纠正的订单数
    private int updateCount;

    // 本地没有的订单
    private int noneCount;
}
