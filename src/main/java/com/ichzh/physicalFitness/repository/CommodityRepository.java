package com.ichzh.physicalFitness.repository;

import com.ichzh.physicalFitness.model.Commodity;
import com.ichzh.physicalFitness.repository.ext.ICommodityRepositoryExt;

import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 商品dao层
 */
@Repository
public interface CommodityRepository extends BaseRepository<Commodity, String>, ICommodityRepositoryExt {

    Commodity findByTownAndServiceBlockAndApiCode(Integer town, Integer serviceBlock, Integer apiCode);

    Commodity findByCommodityId(String commodityId);

    List<Commodity> findByCommodityKind(Integer commodityKind);
}
