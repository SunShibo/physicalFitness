package com.ichzh.physicalFitness.service.impl;

import com.ichzh.physicalFitness.domain.PayOrderParam;
import com.ichzh.physicalFitness.model.GoodsOrder;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.service.CommodityKindService;
import com.ichzh.physicalFitness.service.PayBeanService;
import com.ichzh.physicalFitness.service.UserService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 会员商品服务
 */
@Service("memberCommodity")
public class MemberCommodityKindServiceImpl implements CommodityKindService {

    @Resource(name = "userService")
    private UserService userService;

    @Override
    public void modifyMemberByGoodsOrder(GoodsOrder goodsOrder, Member member) {
        // 订单状态
        Integer orderStatus = goodsOrder.getOrderStatus();
        // 订单状态为已支付时，更新会员表，改为付费会员
        if (Integer.valueOf(2).equals(orderStatus)) {
            member.setMemberGrade(2);
            member.setExpiryDateBegin(goodsOrder.getOrderTime());
            member.setExpiryDateEnd(goodsOrder.getValidTime());
            userService.modifyUser(member);
            return;
        }
        // 订单取消，则改为普通会员
        if (Integer.valueOf(3).equals(orderStatus)) {
            member.setMemberGrade(1);
            member.setExpiryDateBegin(null);
            member.setExpiryDateEnd(null);
            userService.modifyUser(member);
        }
    }

    @Override
    public PayBeanService checkDiscounts(PayOrderParam payOrderParam) {
        payOrderParam.setDiscountAmount(new BigDecimal("0"));
        return PayBeanService.getInstance();
    }
}
