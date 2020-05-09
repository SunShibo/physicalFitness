package com.ichzh.physicalFitness.repository;

import com.ichzh.physicalFitness.model.GoodsOrder;
import com.ichzh.physicalFitness.repository.ext.IGoodsOrderRepositoryExt;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsOrderRepository extends BaseRepository<GoodsOrder, String>, IGoodsOrderRepositoryExt {

    GoodsOrder findByOrderId(String orderId);

    GoodsOrder findByOrderIdAndMemberId(String orderId, String memberId);

    List<GoodsOrder> findByCommodityIdAndMemberId(String commodityId, String memberId);

    int deleteByOrderId(String orderId);

}
