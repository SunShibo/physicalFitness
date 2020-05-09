package com.ichzh.physicalFitness.service;

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.Commodity;

import java.util.List;

public interface CommodityService {

    /**
     * 根据信息所属区、服务模块以及信息类型查询商品信息
     * @param town
     * @param serviceBlock
     * @param apiCode
     * @return
     */
    Commodity queryCommodityByTownAndServiceBlockAndApiCode(Integer town, Integer serviceBlock, Integer apiCode);

    /**
     * 校验商品是否存在，如果存在，则返回商品信息
     * @param commodity
     * @return
     */
    Commodity queryCommodity(Commodity commodity, OperaResult operaResult);

    /**
     * 根据商品id查询商品信息
     * @param commodityId
     * @return
     */
    Commodity queryCommodityByCommodityId(String commodityId);

    /**
     * 根据商品类型查询商品列表
     * @param commodityKind
     * @return
     */
    List<Commodity> queryCommodityByCommodityKind(Integer commodityKind);
}
