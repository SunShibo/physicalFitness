package com.ichzh.physicalFitness.repository.ext.impl;

import com.ichzh.physicalFitness.model.Commodity;
import com.ichzh.physicalFitness.repository.ext.ICommodityRepositoryExt;
import com.yodoo.spring.dynamicquery.BaseRepositoryExtImpl;
import com.yodoo.spring.dynamicquery.ISqlElement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CommodityRepositoryImpl extends BaseRepositoryExtImpl implements ICommodityRepositoryExt {

    @Override
    public Commodity selectCommodityByCommodityId(String commodityId) {
        Map<String, Object> params = new HashMap<>();
        params.put("commodityId", commodityId);
        ISqlElement sqlElement = this.processSql(params, "CommodityRepositoryImpl.selectCommodityByCommodityId");
        List<Commodity> list = this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(Commodity.class));
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
	     * 查询每个api对应的免费使用次数
	 * @param apiCode
	 * @return
	 */
	public int queryFreeNumOfCommodity(Integer apiCode) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("apiCode", apiCode);
		
		ISqlElement sqlElement = this.processSql(params, "CommodityRepositoryImpl.queryFreeNumOfCommodity");
		return this.jdbcTemplate.queryForObject(sqlElement.getSql(), Integer.class, sqlElement.getParams());
	}
    
    
}
