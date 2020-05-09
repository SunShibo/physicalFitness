package com.ichzh.physicalFitness.service.impl;

import com.ichzh.physicalFitness.conf.LoginConfig;
import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.domain.PayOrderParam;
import com.ichzh.physicalFitness.model.GoodsOrder;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.repository.MemberRepository;
import com.ichzh.physicalFitness.service.CommodityKindService;
import com.ichzh.physicalFitness.service.PayBeanService;
import com.ichzh.physicalFitness.service.UserService;
import com.ichzh.physicalFitness.util.ApplicationContextProviderUtil;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

/**
 * api商品服务
 */
@Service("apiCommodity")
public class ApiCommodityKindServiceImpl implements CommodityKindService {

    @Resource(name = "userService")
    private UserService userService;

    @Override
    public void modifyMemberByGoodsOrder(GoodsOrder goodsOrder, Member member) {
        // 学豆数量
        Integer payBeansNum = goodsOrder.getPayBeansNum();
        // 使用的学豆数量如果为0，则无需处理
        if (payBeansNum <= 0) {
            return;
        }
        // 订单状态
        Integer orderStatus = goodsOrder.getOrderStatus();
        // 未支付状态，则扣除学豆
        if (Integer.valueOf(1).equals(orderStatus)) {
            member.setMemberBeans(member.getMemberBeans() - goodsOrder.getPayBeansNum());
            userService.modifyUser(member);
            return;
        }
        // 取消状态，则返还学豆
        if (Integer.valueOf(3).equals(orderStatus)) {
            member.setMemberBeans(member.getMemberBeans() + goodsOrder.getPayBeansNum());
            userService.modifyUser(member);
        }
    }

    @Override
    public PayBeanService checkDiscounts(PayOrderParam payOrderParam) {
        // 学豆数量
        int payBeansNum = payOrderParam.getPayBeansNum();
        PayBeanService payBeanService = PayBeanService.getInstance();
        if (payBeansNum == 0) {
            payOrderParam.setDiscountAmount(new BigDecimal("0"));
            return payBeanService;
        }
        // 学豆是否足够
        if (payOrderParam.getMember().getMemberBeans() < payBeansNum) {
            OperaResult operaResult = payOrderParam.getOperaResult();
            operaResult.setResultDesc("学豆不够");
            operaResult.setData(payOrderParam.getMember().getMemberBeans());
            return null;
        }
        // 学豆转为金钱，是否超出商品价格
        BigDecimal discountAmount = payBeanService.calculationAmountByPayBeansNum(payBeansNum);
        if (discountAmount.compareTo(payOrderParam.getCommodity().getPrice()) > 0) {
            OperaResult operaResult = payOrderParam.getOperaResult();
            operaResult.setResultDesc("使用学豆过多");
            return null;
        }
        payOrderParam.setDiscountAmount(discountAmount);
        return payBeanService;
    }
}
