package com.ichzh.physicalFitness.service.impl;

import org.springframework.stereotype.Service;

import com.ichzh.physicalFitness.service.PayBeanService;

import java.math.BigDecimal;

@Service("defPayBeanService")
public class DefPayBeanServiceImpl implements PayBeanService {


    @Override
    public BigDecimal calculationAmountByPayBeansNum(int payBeansNum) {
        // 一个学豆抵一块钱
        return new BigDecimal(payBeansNum);
    }
}
