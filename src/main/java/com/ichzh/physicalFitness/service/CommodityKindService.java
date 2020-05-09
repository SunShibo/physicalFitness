package com.ichzh.physicalFitness.service;

import com.ichzh.physicalFitness.domain.PayOrderParam;
import com.ichzh.physicalFitness.model.GoodsOrder;
import com.ichzh.physicalFitness.model.Member;
import com.ichzh.physicalFitness.util.ApplicationContextProviderUtil;

public interface CommodityKindService {

    /**
     * 根据商品类型获取对应的商品服务实现类
     * @param commodityKind
     * @return
     */
    static CommodityKindService getInstance(Integer commodityKind) {
        if (Integer.valueOf(10901).equals(commodityKind)) {
            return ApplicationContextProviderUtil.getBean("memberCommodity", CommodityKindService.class);
        }
        if (Integer.valueOf(10902).equals(commodityKind)) {
            return ApplicationContextProviderUtil.getBean("apiCommodity", CommodityKindService.class);
        }
        return null;
    }

    /**
     * 根据订单信息更新会员信息；订单状态决定如何更新
     * 1.订单商品是api商品，则更新学豆
     * 2.订单商品是会员商品，则更新会员相关的字段
     * @param goodsOrder
     */
    void modifyMemberByGoodsOrder(GoodsOrder goodsOrder, Member member);

    /**
     * 校验优惠条件是否符合要求
     * @param payOrderParam
     * @return
     */
    PayBeanService checkDiscounts(PayOrderParam payOrderParam);
}
