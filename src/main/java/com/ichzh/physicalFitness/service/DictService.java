package com.ichzh.physicalFitness.service;

import java.util.List;
import java.util.Map;

import com.ichzh.physicalFitness.model.Commodity;
import com.ichzh.physicalFitness.model.GoodsOrder;
import com.ichzh.physicalFitness.model.SysDict;

public interface DictService {

    /**
     * 查询信息类型与api映射关系map
     * @return
     */
    Map<String, Integer> queryInfoKindMap();

    /**
     * 根据字典类型获取字典信息
     * @param dictType
     * @return
     */
    List<SysDict> querySysDictByDictType(Integer dictType);

    /**
     * 格式化订单字典信息
     * @param goodsOrder
     */
    void formatGoodsOrderDictName(GoodsOrder goodsOrder);

    /**
     * 格式化商品字典信息
     * @param commodity
     */
    void formatCommodityDictName(Commodity commodity);

    /**
     * 查询所有的区县字典映射
     * @return
     */
    Map<Integer, String> queryAllTownMap();

    /**
     * 根据字典类型查询字典映射
     * 如果字典类型为空，则查询全部
     * @param dictType
     * @return
     */
    Map<Integer, String> querySysDictMapByDictType(Integer dictType);
}
