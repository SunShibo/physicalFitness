package com.ichzh.physicalFitness.repository.ext.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.util.StringUtils;

import com.ichzh.physicalFitness.conf.Constant;
import com.ichzh.physicalFitness.dto.AidedAllocateRangePrimSchoolDto;
import com.ichzh.physicalFitness.dto.SchoolDto;
import com.ichzh.physicalFitness.dto.StreetCommunityDto;
import com.ichzh.physicalFitness.model.AdmissionCondition;
import com.ichzh.physicalFitness.model.AidedAllocateRange;
import com.ichzh.physicalFitness.repository.AidedAllocateRangeRepository;
import com.ichzh.physicalFitness.repository.ext.IAidedAllocateRangeRepositoryExt;
import com.yodoo.spring.dynamicquery.BaseRepositoryExtImpl;
import com.yodoo.spring.dynamicquery.ISqlElement;

public class AidedAllocateRangeRepositoryImpl extends BaseRepositoryExtImpl
		implements IAidedAllocateRangeRepositoryExt {

	@Autowired
	private AidedAllocateRangeRepository aidedAllocateRangeRepository;
	
	/**
	 * 查询派位范围
	 * @param serviceBlock         派位范围所属服务模块
	 * @param town                 派位范围所属区
	 * @param schoolDistrictName   学区或片区名称
	 * @param schoolCode           小学\中学学校代码
	 * @param schoolName           小学\中学名称代码
	 * @return
	 */
	@Override
	public List<AidedAllocateRange> queryBy(Integer serviceBlock, Integer town, String schoolDistrictName,
			String schoolCode, String schoolName) {
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("town", town);
		params.put("schoolDistrictName", schoolDistrictName);
		params.put("schoolCode", schoolCode);
		params.put("schoolName", schoolName);
		params.put("functionCode", Constant.FUNCTION_CODE_10707);
		
		ISqlElement sqlElement = this.processSql(params, "AidedAllocateRangeRepositoryImpl.queryBy.query");
		
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(AidedAllocateRange.class));
	}

	@Override
	public List<StreetCommunityDto> querySchoolDistrict(Integer serviceBlock, Integer town, String schoolDistrictName) {
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("town", town);
		params.put("schoolDistrictName", schoolDistrictName);
		params.put("functionCode", Constant.FUNCTION_CODE_10707);
		
		ISqlElement sqlElement = this.processSql(params, "AidedAllocateRangeRepositoryImpl.querySchoolDistrict.query");
		
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(StreetCommunityDto.class));
	}

	@Override
	public List<SchoolDto> querySchoolName(Integer serviceBlock, Integer town, String schoolDistrictName,
			String schoolCode, String schoolName) {
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("town", town);
		params.put("schoolDistrictName", schoolDistrictName);
		params.put("schoolCode", schoolCode);
		params.put("schoolName", schoolName);
		params.put("functionCode", Constant.FUNCTION_CODE_10707);
		
		ISqlElement sqlElement = this.processSql(params, "AidedAllocateRangeRepositoryImpl.querySchoolName.query");
		
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(SchoolDto.class));
	}

	@Override
	public String batchUpdateTable() {
		List<AidedAllocateRange> list = aidedAllocateRangeRepository.findAll();
		for (AidedAllocateRange aidedAllocateRange : list) {
			String primarySchoolName = aidedAllocateRange.getPrimarySchoolName();
			String middleSchoolName = aidedAllocateRange.getJuniorMiddleName();
			//String primarySchoolCode = aidedAllocateRange.getPrimarySchoolCode();
			//String middleSchoolCode = aidedAllocateRange.getJuniorMiddleCode();
			Integer town = aidedAllocateRange.getTown();
			Integer serviceBlock = aidedAllocateRange.getServiceBlock();
			if(StringUtils.hasText(primarySchoolName)) {
				List<Map<String,Object>> schoolList = jdbcTemplate.queryForList("SELECT * FROM school_choice WHERE school_name LIKE ?"
						+ " and service_block = ? and town = ?",
						new Object[] {"%"+primarySchoolName+"%",serviceBlock,town});
				if(schoolList.size() == 1) {
					String code = String.valueOf(schoolList.get(0).get("school_code"));
					jdbcTemplate.update("update aided_allocate_range set primary_school_code = ? where "
							+ "primary_school_name = ? and service_block = ? and town = ?", new Object[] {code,primarySchoolName,serviceBlock,town});
				}
			}
			if(StringUtils.hasText(middleSchoolName)) {
				List<Map<String,Object>> schoolList = jdbcTemplate.queryForList("SELECT * FROM school_choice WHERE school_name LIKE ?"
						+ " and service_block = ? and town = ?",
						new Object[] {"%"+middleSchoolName+"%",serviceBlock,town});
				if(schoolList.size() == 1) {
					String code = String.valueOf(schoolList.get(0).get("school_code"));
					jdbcTemplate.update("update aided_allocate_range set junior_middle_code = ? where "
							+ "junior_middle_name = ? and service_block = ? and town = ?", new Object[] {code,middleSchoolName,serviceBlock,town});
				}
			}
		}
		return "ok";
	}

	/**
	 * 查询派位范围中的小学
	 * @param serviceBlock  派位服务模块.
	 * @param town          派位所属区.
	 * @param schoolName    小学名称关键字 这里采用like查询
	 * @return List<AidedAllocateRangePrimSchoolDto>.
	 */
	public List<AidedAllocateRangePrimSchoolDto> queryAidedAllocateRangePrimSchoolBy(Integer serviceBlock, Integer town, String schoolName) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("town", town);
		params.put("schoolName", schoolName);
		params.put("functionCode", Constant.FUNCTION_CODE_10707);
		
		ISqlElement sqlElement = this.processSql(params, "AidedAllocateRangeRepositoryImpl.queryAidedAllocateRangePrimSchoolBy");
		
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(AidedAllocateRangePrimSchoolDto.class));
	}

	/**
	 * 查询派位范围中的中学
	 * @param serviceBlock  派位服务模块.
	 * @param town          派位所属区.
	 * @param priSchCode    小学学校代码
	 * @return List<AidedAllocateRangePrimSchoolDto>.
	 */
	public List<AidedAllocateRangePrimSchoolDto> queryAidedAllocateRangeMiddSchoolBy(Integer serviceBlock, Integer town,
			String priSchCode) {
		
		Map<String, Object> params = new HashMap<>();
		params.put("serviceBlock", serviceBlock);
		params.put("town", town);
		params.put("priSchCode", priSchCode);
		params.put("functionCode", Constant.FUNCTION_CODE_10707);
		
		ISqlElement sqlElement = this.processSql(params, "AidedAllocateRangeRepositoryImpl.queryAidedAllocateRangeMiddSchoolBy");
		
		return this.jdbcTemplate.query(sqlElement.getSql(), sqlElement.getParams(), new BeanPropertyRowMapper<>(AidedAllocateRangePrimSchoolDto.class));
	}
	
	

	
	
}
