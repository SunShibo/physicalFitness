package com.ichzh.physicalFitness.service;

import java.math.BigDecimal;

import com.ichzh.physicalFitness.util.ApplicationContextProviderUtil;

/**
 * 学豆业务
 */
public interface PayBeanService {

    /**
     * 根据不同条件获取不同的学豆实现类
     * 预留拓展，使用多态实现
     * @return
     */
    static PayBeanService getInstance() {
        return ApplicationContextProviderUtil.getBean("defPayBeanService", PayBeanService.class);
    }

    /**
     * 学豆转换成金额
     * @param payBeansNum 学豆数量
     * @return
     */
    BigDecimal calculationAmountByPayBeansNum(int payBeansNum);
}
