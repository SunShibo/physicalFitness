package com.ichzh.physicalFitness.service.impl;

import com.ichzh.physicalFitness.domain.OperaResult;
import com.ichzh.physicalFitness.model.Commodity;
import com.ichzh.physicalFitness.repository.CommodityRepository;
import com.ichzh.physicalFitness.service.CommodityService;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service("commodityService")
@Slf4j
public class CommodityServiceImpl implements CommodityService {

    @Resource(name = "commodityRepository")
    private CommodityRepository commodityRepository;

    @Override
    public Commodity queryCommodityByTownAndServiceBlockAndApiCode(Integer town, Integer serviceBlock, Integer apiCode) {
        return commodityRepository.findByTownAndServiceBlockAndApiCode(town, serviceBlock, apiCode);
    }

    @Override
    public Commodity queryCommodity(Commodity commodity, OperaResult operaResult) {
        operaResult.setMessageTitle("下单失败");
        operaResult.setMessageType("error");
        if (commodity == null || StringUtils.isEmpty(commodity.getCommodityId())) {
            operaResult.setResultDesc("未识别的商品");
            return null;
        }
        commodity = commodityRepository.findByCommodityId(commodity.getCommodityId());
        if (commodity == null) {
            operaResult.setResultDesc("商品不存在");
            return null;
        }
        // 商品是否下架
        if (!Integer.valueOf(1).equals(commodity.getIsUsed())) {
            operaResult.setResultDesc("商品已下架");
            return null;
        }
        // 如果商品免费，则无需购买
        if (new BigDecimal("0").compareTo(commodity.getPrice()) == 0) {
            operaResult.setResultDesc("商品免费，无需购买");
            return null;
        }
        // 学豆默认值设置
        if (commodity.getPayBeansNum() == null || commodity.getPayBeansNum() < 0) {
            commodity.setPayBeansNum(0);
        }
        return commodity;
    }

    @Override
    public Commodity queryCommodityByCommodityId(String commodityId) {
        return commodityRepository.findByCommodityId(commodityId);
    }

    @Override
    public List<Commodity> queryCommodityByCommodityKind(Integer commodityKind) {
        return commodityRepository.findByCommodityKind(commodityKind);
    }
}
