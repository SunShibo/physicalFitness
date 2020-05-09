package com.ichzh.physicalFitness.repository.ext;

import com.ichzh.physicalFitness.model.Commodity;

public interface ICommodityRepositoryExt {

    Commodity selectCommodityByCommodityId(String commodityId);
    /**
         * 查询每个api对应的免费使用次数
     * @param apiCode
     * @return
     */
    public int queryFreeNumOfCommodity(Integer apiCode);
}
